import {Component, ViewContainerRef} from '@angular/core';
import {BottomToastsManager} from "../../Shared/CustomToaster";
import {PagingModel} from "app/Shared/MaterialComponent/paging/paging.model";
import {ModalComponent} from "../../Shared/MaterialComponent/modal/modal.component";
import {SearchedgeEntity} from "./edge.SearchEntity";
import {EdgeService} from "./edge.Service";
import {EdgeEntity} from "./edge.Entity";
import {ShapeEntity} from "../shape/shape.Entity";

@Component({
    selector: 'App-edge',
    templateUrl: './edge.Component.html',
    styleUrls: ['./edge.Component.css'],
    providers: [EdgeService, BottomToastsManager]
})
export class edgeComponent {
    Title: string = "edge";
    edgeEntities: EdgeEntity[];
    PagingModel = new PagingModel(7, 10, data => {
        this.edgeService.Get().subscribe(p => {
            this.edgeEntities = p;
        });
    });
    temp = {};
    CreatededgeEntity: EdgeEntity;
    EditededgeEntity: EdgeEntity;
    EditPosition: number;
    SearchedgeEntity: SearchedgeEntity;
    DeleteData: EdgeEntity;
    EditModalComponent: ModalComponent;
    DeleteModalComponent: ModalComponent;
    shapeList: Array<ShapeEntity> = [];
    ChoosedshapeItem: ShapeEntity = new ShapeEntity();
    SearchshapeName: string;

    constructor(private edgeService: EdgeService,
                private toastr: BottomToastsManager, vcr: ViewContainerRef) {
        this.CreatededgeEntity = new EdgeEntity();
        this.EditededgeEntity = new EdgeEntity();
        this.SearchedgeEntity = new SearchedgeEntity();
        this.Search();
        this.EditModalComponent = new ModalComponent();
        this.DeleteModalComponent = new ModalComponent();
        this.toastr.setRootViewContainerRef(vcr);
    }

    Search() {
        this.SearchedgeEntity.Skip = this.PagingModel.Take * this.PagingModel.Active;
        this.SearchedgeEntity.Take = this.PagingModel.Take;
        this.edgeService.Get(this.SearchedgeEntity).subscribe(p => {
            this.edgeEntities = p;
            this.Count();
        });
    }

    Count() {
        this.edgeService.Count(this.SearchedgeEntity).subscribe(data => {
            this.PagingModel.TotalPage = Math.ceil(data / this.PagingModel.Take);
        });
    }

    LoadDataToUpdateModal(edge: EdgeEntity, index: number) {
        this.EditededgeEntity = new EdgeEntity(edge);
        this.EditPosition = index;
        document.getElementById(this.EditModalComponent.ID).click();
    }

    LoadDataToDelete(edge: EdgeEntity) {
        this.DeleteData = edge;
        document.getElementById(this.DeleteModalComponent.ID).click();
    }

    Edit() {
        this.edgeService.Update(this.EditededgeEntity).subscribe(p => {
            this.edgeEntities[this.EditPosition] = p;
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Add() {
        this.edgeService.Create(this.CreatededgeEntity).subscribe(p => {
            p.IsEdit = true;
            this.edgeEntities.unshift(p);
            this.CreatededgeEntity = new EdgeEntity();
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Delete() {
        this.edgeService.Delete(this.DeleteData).subscribe(p => {
            let indexOf = this.edgeEntities.indexOf(this.DeleteData);
            this.edgeEntities.splice(indexOf, 1);
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    // Save(edgeEntity: EdgeEntity) {
    //     if (edgeEntity.Id === undefined || edgeEntity.Id === null) {
    //         this.EdgeService.Create(edgeEntity).subscribe(p => {
    //             Object.assign(edgeEntity, p);
    //             edgeEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     } else {
    //         this.EdgeService.Update(edgeEntity).subscribe(p => {
    //             Object.assign(edgeEntity, p);
    //             edgeEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     }
    // }
    //
    // Cancel(edgeEntity: EdgeEntity) {
    //     if (edgeEntity.Id === undefined || edgeEntity.Id === null) {
    //         this.edgeEntities.splice(0, 1);
    //     } else {
    //         Object.assign(edgeEntity, this.temp);
    //         edgeEntity.IsEdit = false;
    //     }
    // }
}