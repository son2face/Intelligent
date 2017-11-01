import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import * as XLSX from 'xlsx';
import {ExcelEntity} from "./Excel.Entity";

type AOA = Array<Array<any>>;

@Component({
    selector: 'Excel',
    templateUrl: './Excel.component.html'
})

export class ExcelComponent implements OnInit {
    Id: string = this.MakeRandomId();
    @Input() ExcelEntity: ExcelEntity = new ExcelEntity();
    @Output() onFileChanged: EventEmitter<ExcelEntity> = new EventEmitter();

    constructor() {
    }

    @Input('FileId')
    set Innit(value: string) {
        if (value !== undefined)
            this.Id = value;
        else console.warn("FileId Field isn't setted !")
    }

    ngOnInit() {
    }

    OpenFile() {
        document.getElementById(this.Id).click();
    }

    data: AOA = [[1, 2], [3, 4]];

    LoadFile(evt) {
        const target: DataTransfer = <DataTransfer>(evt.target);
        if (target.files.length != 1) {
            throw new Error("Cannot upload multiple files on the entry")
        }
        const reader = new FileReader();
        reader.onload = (e: any) => {
            /* read workbook */
            const bstr = e.target.result;
            const wb = XLSX.read(bstr, {type: 'binary'});

            /* grab first sheet */
            const wsname = wb.SheetNames[0];
            const ws = wb.Sheets[wsname];
            /* save data */
            let data: AOA = XLSX.utils.sheet_to_json(ws, {header: 1});
            if (data == null) {
                console.warn("Cannot read file or file is empty!");
                return;
            }
            this.ExcelEntity.ParseExcel(data);
            // if (data.length == 1 || data[0].length <= this.EndCol) {
            //     console.warn("Empty data or invalid format xls!");
            //     return;
            // }
            // let DataName = [];
            // for (let i = this.StartCol; i <= this.EndCol; i++) {
            //     DataName.push(data[i]);
            // }
            // let Unique = [...new Set(DataName)];
            // if (DataName.length != Unique.length) {
            //     console.warn("Duplicate ColName!");
            //     return;
            // }
            // if (this.NameDict != null) {
            //     for (let i = 0; i < DataName.length; i++) {
            //         if (this.NameDict[DataName[i].toLowerCase()] == null) {
            //             console.warn("Cannot mapping col!");
            //             return;
            //         }
            //         DataName[i] = this.NameDict[DataName[i].toLowerCase()];
            //     }
            // }
            // for (let i = 1; i < data.length; i++) {
            //     let a: any = {};
            //     let row: Array<any> = data[i];
            //     for (let j = this.StartCol; j < this.EndCol; j++) {
            //         a[DataName[j]] = row[j];
            //         this.ExcelEntity.Data.push(a);
            //     }
            // }
            // console.log(this.ExcelEntity);
            this.ExcelEntity.Name = target.files[0].name;
            this.ExcelEntity.Length = target.files[0].size;
            let Arr = this.ExcelEntity.Name.split('.');
            this.ExcelEntity.Extension = Arr.length > 1 ? Arr[Arr.length - 1] : Arr[0];
            this.onFileChanged.emit(this.ExcelEntity);
        };

        reader.readAsBinaryString(target.files[0]);
    }

    MakeRandomId(): string {
        let text = "";
        let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (let i = 0; i < 5; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        return text;
    }
}

