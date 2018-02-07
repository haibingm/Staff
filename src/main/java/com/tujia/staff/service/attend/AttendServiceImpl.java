package com.tujia.staff.service.attend;

import com.tujia.staff.common.utils.DateUtils;
import com.tujia.staff.dao.attend.AttendMapper;
import com.tujia.staff.model.PageQueryBean;
import com.tujia.staff.model.attend.Attend;
import com.tujia.staff.vo.attend.AttendQueryCondition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by JackWangon[www.aiprogram.top] 2017/6/20.
 */
@Service("attendServiceImpl")
public class AttendServiceImpl implements AttendService{

    private Log log = LogFactory.getLog(AttendServiceImpl.class);


    @Autowired
    private AttendMapper attendMapper;


    //获取当天打卡记录
    @Override
    public Attend checkTodaySign(Long userId) {
        return attendMapper.selectTodaySignRecord(userId);
    }

    @Override
    public Attend findAttendById(Long id) {
        return attendMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateBySelectKey(Attend attend) {
        return attendMapper.updateByPrimaryKeySelective(attend);
    }

    //打卡签到
    @Override
    public void signAttend(Attend attend) throws Exception {
        try {
            Date today = new Date();
            attend.setAttendDate(today);
            attend.setAttendWeek((byte) DateUtils.getTodayWeek());
            //查询当天 此人有没有打卡记录
            Attend todayRecord=attendMapper.selectTodaySignRecord(attend.getUserId());
            if(todayRecord==null){
                //无打卡记录则存为签到时间
                attend.setAttendMorning(today);
                attend.setWorkTime(0);
                attend.setAbsence(0);
                attendMapper.insertSelective(attend);
            }else if(todayRecord.getAttendMorning() == null && todayRecord.getAttendEvening()==null){
                todayRecord.setAttendMorning(today);
                todayRecord.setWorkTime(0);
                todayRecord.setAbsence(0);
                attendMapper.updateByPrimaryKeySelective(todayRecord);
            }else{
                //存为签退时间
                todayRecord.setAttendEvening(today);
                //Date attendMorning = todayRecord.getAttendMorning();
                //todayRecord.setWorkTime(DateUtils.getMunite(attendMorning,today));
                todayRecord.setAbsence(0);
                attendMapper.updateByPrimaryKeySelective(todayRecord);
            }
        }catch (Exception e){
            log.error("用户签到异常",e);
            throw e;
        }
    }

    @Override
    public PageQueryBean listAttend(AttendQueryCondition condition) {
        //根据条件查询 count记录数目
        int count = attendMapper.countByCondition(condition);
        PageQueryBean pageResult = new PageQueryBean();
        if(count>0){
            pageResult.setTotalRows(count);
            pageResult.setCurrentPage(condition.getCurrentPage());
            pageResult.setPageSize(condition.getPageSize());
            List<Attend> attendList =  attendMapper.selectAttendPage(condition);
            pageResult.setItems(attendList);
        }
        //如果有记录 才去查询分页数据 没有相关记录数目 没必要去查分页数据
        return pageResult;
    }
}
