export class DataEntity {
    name: string;
    data: string;
    length: number;
    extension: string;

    constructor(DataEntity?: any) {
        if (DataEntity == null) {
            this.name = null;
            this.data = null;
            this.length = null;
            this.extension = null;
        } else {
            this.name = DataEntity.name;
            this.data = DataEntity.data;
            this.length = DataEntity.length;
            this.extension = DataEntity.extension;
        }
    }

    GetKB(): number {
        let Result = Math.round(this.length / 1024);
        return isNaN(Result) ? 0 : Result;
    }

    GetMB(): number {
        let Result = Math.round(this.length / 1024 / 1024);
        return isNaN(Result) ? 0 : Result;
    }
}
