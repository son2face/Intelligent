import {Component, ContentChild, EventEmitter, Input, Output, TemplateRef} from '@angular/core';

@Component({
    selector: 'app-dropdown',
    templateUrl: './dropdown2.component.html',
    styleUrls: ['./dropdown2.component.css']
})

export class Dropdown2Component {
    @ContentChild(TemplateRef) itemTemplate: TemplateRef<any>;
    @Input() SelectedDisplay: string;
    @Input() Placeholder: string;
    @Output() Return = new EventEmitter<any>();
    @Output() KeySearch = new EventEmitter<any>();
    @Input() PlaceHolder: string = "";
    IsShow = false;
    IsError = false;


    _Items: any;

    @Input('Items')
    set Items(value: any) {
        this._Items = value;
        this.IsError = false;
        if (this._Items !== undefined) {
            if (this._Items === null || this._Items.length === 0) {
                this.IsError = true;
            }
        }
    }

    GetItem(item) {
        this.IsShow = false;
        this.SelectedDisplay = item.Name;
        this.Return.emit(item);
    }

    GetKey(event) {
        this.KeySearch.emit(event.target.value);
    }

    MouseOut(event) {
        this.IsShow = false;
    }

    ngOninit() {
        this.IsError = this.Items == null || this.Items == undefined || this.Items.length === 0;
    }

    CheckShowHide() {
        if (this._Items.length > 0) {
            this.IsShow = true;
        } else this.IsShow = false;
    }
}