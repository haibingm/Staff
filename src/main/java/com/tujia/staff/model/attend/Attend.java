package com.tujia.staff.model.attend;

import lombok.Data;

import java.util.Date;

@Data
public class Attend {
    private Long id;

    private Long userId;

    private Date attendDate;

    private Byte attendWeek;

    private Date attendMorning;

    private Date attendEvening;

    private Integer workTime;

    private Integer absence;

    private Byte attendStatus;
}