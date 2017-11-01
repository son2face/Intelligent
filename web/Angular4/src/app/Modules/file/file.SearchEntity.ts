import {FilterEntity} from "../../Shared/Filter.Entity";

export class SearchfileEntity extends FilterEntity {
    fileId: number;
    name: string;
    data: string;
    createdTime: string;
    type: string;
    expriedTime: string;
    userId: string;

    constructor(file: any = null) {
        super(file);
        this.fileId = file == null ? null : file.fileId;
        this.name = file == null ? null : file.name;
        this.data = file == null ? null : file.data;
        this.createdTime = file == null ? null : file.createdTime;
        this.type = file == null ? null : file.type;
        this.expriedTime = file == null ? null : file.expriedTime;
        this.userId = file == null ? null : file.userId;
    }
}