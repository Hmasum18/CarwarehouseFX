/*
 * Copyright (c) 2020. Hasan Masum
 * github: https://github.com/Hmasum18
 * You can copy the code but please don't forget to give proper credit
 */

package github.hmasum18.carshowroomfrontend.repository;

public class DataChangeInfo {
    private final DataChangeType dataChangeType;
    private final ChangeVerdict changeVerdict;
    private final DataChanger dataChanger;
    private final ChangedDataType changedDataType;

    public DataChangeInfo(DataChangeType dataChangeType, ChangeVerdict changeVerdict, DataChanger dataChanger, ChangedDataType changedDataType) {
        this.dataChangeType = dataChangeType;
        this.changeVerdict = changeVerdict;
        this.dataChanger = dataChanger;
        this.changedDataType = changedDataType;
    }

    public DataChangeType getDataChangeType() {
        return dataChangeType;
    }

    public ChangeVerdict getChangeVerdict() {
        return changeVerdict;
    }

    public DataChanger getDataChanger() {
        return dataChanger;
    }

    public ChangedDataType getChangedDataType() {
        return changedDataType;
    }
}
