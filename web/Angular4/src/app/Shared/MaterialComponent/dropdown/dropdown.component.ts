import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'dropdown',
    templateUrl: './dropdown.component.html',
    styleUrls: ['./dropdown.component.css']
})

export class DropdownComponent {
    IsShow = false;
    static DropdownComponentList: Array<DropdownComponent> = [];
    @Input() isMultiSelect: boolean = false;
    @Input() isTurnOffAnother: boolean = true;
    Id: string = this.MakeRandomId();
    static IsBlock: boolean = false;
    @Input() DataList: Array<any> = [{}];
    @Output() onEnter: EventEmitter<any> = new EventEmitter();
    @Input() ChangeWhenUpDown: boolean = false;
    @Input() IsLoopOption: boolean = false;
    CurrentNumber: number = -1;
    @Input() IsValidateDataList: boolean = true;
    @Input() PropertyActive: string = "IsActive";
    @Input() PropertySelected: string = "IsSelected";
    public IsFirstClick : boolean = true;
    @Input() IsTree: boolean = false;
    @Input() IsError: boolean = false;
    @Output() onFirstClick : EventEmitter<any> = new EventEmitter();
    @Input() ClassContent : string = "";
    constructor() {
        DropdownComponent.DropdownComponentList.push(this);
    }

    MakeRandomId(): string {
        let text = "";
        let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (let i = 0; i < 5; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        return text;
    }

    TurnOn() {
        this.IsShow = true;
        DropdownComponent.IsBlock = true;
        setTimeout(() => {
            DropdownComponent.IsBlock = false;
            this.TurnOffAnother();
        }, 40);
    }

    Click() {
        if(this.IsFirstClick){
            this.IsFirstClick = false;
            this.onFirstClick.emit();
        }
        if (this.IsShow) {
            this.IsShow = false;
            return;
        }
        this.Remove();
        this.ValidateDataList();
        this.TurnOn();
    }

    Esc() {
        if (this.IsShow) {
            this.IsShow = false;
            return;
        }
        this.Remove();
        this.TurnOn();
    }
    // DbClick() {
    //     this.Remove();
    //     this.ValidateDataList();
    //     this.TurnOn();
    // }
    TurnOff(event?) {
        if (this.isMultiSelect || this.IsTree) {
            this.TurnOn();
            return;
        }
        console.log(event);
        this.IsShow = false;
    }

    TurnOffAnother() {
        if (this.isTurnOffAnother == false || DropdownComponent.IsBlock) return;
        DropdownComponent.DropdownComponentList.forEach(DC => {
            if (DC.Id != this.Id) DC.IsShow = false;
        })
    }

    Up() {
        if (this.DataList == null || this.DataList.length == 0) return false;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            if (this.DataList[i][this.PropertyActive] == true) {
                this.DataList[i][this.PropertyActive] = false;
                if (i == 0) {
                    if (this.IsLoopOption) this.DataList[length - 1][this.PropertyActive] = true;
                    else  this.DataList[0][this.PropertyActive] = true;
                } else {
                    this.DataList[i - 1][this.PropertyActive] = true;
                }
                if (this.ChangeWhenUpDown == true && this.isMultiSelect == false) {
                    this.Enter();
                    this.TurnOn();
                }
                return false;
            }
        }
        this.DataList[0][this.PropertyActive] = true;
        if (this.ChangeWhenUpDown == true && this.isMultiSelect == false) {
            this.Enter();
            this.TurnOn();
        }
        return false;
    }

    Down() {
        if (this.DataList == null || this.DataList.length == 0) return false;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            if (this.DataList[i][this.PropertyActive] == true) {
                this.DataList[i][this.PropertyActive] = false;
                if (i == length - 1) {
                    if (this.IsLoopOption) this.DataList[0][this.PropertyActive] = true;
                    else  this.DataList[length-1][this.PropertyActive] = true;
                } else {
                    this.DataList[i + 1][this.PropertyActive] = true;
                }
                if (this.ChangeWhenUpDown == true && this.isMultiSelect == false) {
                    this.Enter();
                    this.TurnOn();
                }
                return false;
            }
        }
        this.DataList[0][this.PropertyActive] = true;
        if (this.ChangeWhenUpDown == true && this.isMultiSelect == false) {
            this.Enter();
            this.TurnOn();
        }
        return false;
    }

    Remove() {
        if (this.DataList == null) return;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            this.DataList[i][this.PropertyActive] = false;
        }
        // if(!this.isMultiSelect) {
        //     for (let i = 0; i < length; i++) {
        //         this.DataList[i].IsSelected = false;
        //     }
        // }
    }

    Enter() {
        if (this.DataList == null) return;
        let length = this.DataList.length;
        for (let i = 0; i < length; i++) {
            if (this.DataList[i][this.PropertyActive] == true) {
                if (!this.isMultiSelect) {
                    for (let j = 0; j < length; j++) {
                        if (this.DataList[j].IsSelected == true) {
                            this.DataList[j].IsSelected = false;
                            break;
                        }
                    }
                    this.CurrentNumber = i;
                    this.DataList[i].IsSelected = true;
                } else {
                    this.CurrentNumber = i;
                    this.DataList[i].IsSelected = !this.DataList[i].IsSelected;
                }
                this.TurnOff();
                this.onEnter.emit(this.DataList[i]);
                return;
            }
        }
        // if (this.DataList == null && this.DataList.length <= this.CurrentNumber) return;
        // let length = this.DataList.length;
        // for (let i = 0; i < length; i++) {
        //     if (this.DataList[i][this.PropertyCheck] == true) {
        //         if (!this.isMultiSelect) {
        //             for (let j = 0; j < length; j++) {
        //                 if (this.DataList[j].IsSelected == true) {
        //                     this.DataList[j].IsSelected = false;
        //                     break;
        //                 }
        //             }
        //         }
        //         this.DataList[i].IsSelected = true;
        //         this.TurnOff();
        //         this.onEnter.emit(this.DataList[i]);
        //         return;
        //     }
        // }
    }

    ValidateDataList() {
        if (this.DataList == null || !this.IsValidateDataList || this.isMultiSelect) return;
        let length = this.DataList.length;
        let count = 0;
        for (let j = 0; j < length; j++) {
            if (this.DataList[j].IsSelected == true) {
                count ++;
            }
        }
        if (this.CurrentNumber < this.DataList.length && count > 1 ) this.DataList[this.CurrentNumber].IsSelected = false;
        for (let j = 0; j < length; j++) {
            if (this.DataList[j].IsSelected == true) {
                this.CurrentNumber = j;
                return;
            }
        }
    }
}