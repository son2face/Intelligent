import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DropdownComponent} from "../dropdown/dropdown.component";

@Component({
    selector: 'TagsInput',
    templateUrl: './tagsinput.component.html',
    styleUrls: ['./tagsinput.component.css']
})

export class TagsinputComponent extends DropdownComponent{
    IsShow = false;
    @Input() DefaultTagClass: string = "label-info";
    @Input() DisplayProperty: string = "Name";
    @Input() RemoveTagClass: string = "";
    @Input() TagLength: number = 15;
    @Input() isTurnOffAnother: boolean = true;
    Id: string = this.MakeRandomId();
    static IsBlock: boolean = false;
    @Input() DataList: Array<any> = [];
    @Output() onEnter: EventEmitter<any> = new EventEmitter();
    @Output() ChangeTag: EventEmitter<any> = new EventEmitter();
    @Input() ChangeWhenUpDown: boolean = false;
    @Input() IsLoopOption: boolean = false;
    CurrentNumber: number = -1;
    @Input() IsValidateDataList: boolean = true;
    @Input() isMultiSelect: boolean = true;
    // @Input() TagList: Array<any> = [];

    constructor() {
        super();
    }

    RemoveTag(Tag: any) {
        //let indexOf = this.DataList.indexOf(Tag);
        //this.DataList.splice(indexOf, 1);
        //this.ChangeTag.emit(Tag);
        Tag.IsSelected = false;
    }

    MakeRandomId(): string {
        let text = "";
        let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (let i = 0; i < 5; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        return text;
    }


}