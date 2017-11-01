import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
    selector: 'app-portlet',
    templateUrl: './Portlet.Component.html',
    styleUrls: ['./Portlet.Component.css']
})

export class PortletComponent {
    @Input() TitleName: string = "";
    @Input() TitleIcon: string = "";
    @Input() HideButton: boolean = true;
    @Output() SearchAction = new EventEmitter<boolean>();
    ToogleSearch: boolean = false;

    Search() {
        this.ToogleSearch = !this.ToogleSearch;
        this.SearchAction.emit(this.ToogleSearch);
    }
}