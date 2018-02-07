package com.tujia.staff.vo.attend;

import lombok.Data;

/**
 * Created by haibingm on 2018/1/18.
 */
@Data
public class AttendVo {
    private String id;

    private String attendId;

    private String attendDate;

    private String reAttendStarter;

    private String reAttendEve;

    private String reAttendMor;

    private String currentHandler;

    private Byte status;

    private String comments;

    private String taskId;

    private String approveFlag;
}
