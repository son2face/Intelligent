import {Component, Input, OnInit} from '@angular/core';


@Component({
    selector: 'app-modal',
    templateUrl: './modal.component.html',
    styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
    ID: string = this.MakeRandomId();
    @Input() OpenModalId: string;
    @Input("Style-width") WidthModal: number = 1440;
    @Input() ModalSize: string = "xl";

    constructor() {
        if (this.ModalSize === undefined) {
            this.ModalSize = 'md';
        }
    }

    ngOnChange() {

    }

    ngOnInit() {
    }

    MakeRandomId(): string {
        let text = "";
        let possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (let i = 0; i < 5; i++)
            text += possible.charAt(Math.floor(Math.random() * possible.length));
        return text;
    }
}
