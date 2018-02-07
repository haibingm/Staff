package com.tujia.staff.vo.attend;


import com.tujia.staff.model.PageQueryBean;
import lombok.Data;

/**
 * Created by haibingm 2018/01/11.
 */
@Data
public class ReAttendQueryCondition extends PageQueryBean {

    private String username;

    private String startDate ;

    private String endDate ;

    private String rangeDate;

    private Byte status;

}
