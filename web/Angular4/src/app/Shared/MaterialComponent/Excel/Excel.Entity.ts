export class ExcelEntity {
    Name: string;
    Data: any;
    Length: number;
    Extension: string;

    constructor(ExcelEntity?: any) {
        if (ExcelEntity == null) {
            this.Name = null;
            this.Data = [];
            this.Length = null;
            this.Extension = null;
        } else {
            this.Name = ExcelEntity.Name;
            this.Data = ExcelEntity.data;
            this.Length = ExcelEntity.length;
            this.Extension = ExcelEntity.extension;
        }
    }

    GetKB(): number {
        let Result = Math.round(this.Length / 1024);
        return Result == NaN ? 0 : Result;
    }

    GetMB(): number {
        let Result = Math.round(this.Length / 1024 / 1024);
        return Result == NaN ? 0 : Result;
    }

    ParseExcel(data) {
    }
}
