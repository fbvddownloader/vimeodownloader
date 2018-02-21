package com.musicdownloader.vimeodailymotiondownloader.entity;

import android.support.annotation.FloatRange;

/**
 * Created by hanhanh.nguyen on 7/13/2017.
 */

public class DownloadMission {
    public DownloadMissionItem entity;
    @ProcessRangeRule
    public float process;

    @FloatRange(from = 0.0, to = 100.0)
    public @interface ProcessRangeRule {}

    /** <br> life cycle. */

    public DownloadMission(DownloadMissionItem entity) {
        this.entity = entity;
        this.process = 0;
    }

    public DownloadMission(DownloadMissionItem entity,
                           @ProcessRangeRule float process) {
        this.entity = entity;
        this.process = process;
    }
}
