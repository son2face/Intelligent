// Nếu cần binds dữ liệu ra ngoài thì gắn 1 Oject PagingModel vào, nếu không, chỉ cần khai báo number, cần nút ... thì dot =  true .

import {Component, EventEmitter, Input, OnInit, Output} from "@angular/core";
import {PagingModel} from "./paging.model";

@Component({
    selector: 'app-paging',
    templateUrl: './paging.component.html',
    styleUrls: ['./paging.component.css'],
})
export class PagingComponent implements OnInit {
    @Input() dot?: boolean;
    @Input() PagingModel?: PagingModel;
    @Input() SetOfPage?: number[] = [10, 20, 50];
    @Output() onChanges: EventEmitter<PagingModel> = new EventEmitter<PagingModel>();

    constructor() {

    }

    previous(): void {
        if (this.valid(this.PagingModel.Active - 1)) {
            this.PagingModel.setActive(this.PagingModel.Active - 1);
        }
    }

    entry(PageToGo: number) {
        if (this.valid(PageToGo)) {
            this.PagingModel.setActive(PageToGo);
            this.onChanges.emit(this.PagingModel);
        }
    }

    next(): void {
        if (this.valid(this.PagingModel.Active + 1)) {
            this.PagingModel.setActive(this.PagingModel.Active + 1);
            this.onChanges.emit(this.PagingModel);
        }
    }

    valid(number: number): boolean {
        return !(number > this.PagingModel.TotalPage - 1 || number < 0);

    }

    SetTake(number: number): boolean {
        this.PagingModel.Take = number;
        this.entry(0);
        this.onChanges.emit(this.PagingModel);
        return false;
    }

    ngOnInit() {

    }

    ngOnChanges() {
        if (this.PagingModel === undefined) this.PagingModel = new PagingModel(10, this.SetOfPage[0]);
        else {
        }
    }
}

