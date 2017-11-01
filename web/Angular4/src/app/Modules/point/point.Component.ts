import {Component, ViewContainerRef} from '@angular/core';
import {BottomToastsManager} from "../../Shared/CustomToaster";
import {PagingModel} from "app/Shared/MaterialComponent/paging/paging.model";
import {ModalComponent} from "../../Shared/MaterialComponent/modal/modal.component";
import {SearchpointEntity} from "./point.SearchEntity";
import {pointService} from "./point.Service";
import {PointEntity} from "./point.Entity";
import {ProblemEntity} from "../problem/problem.Entity";

@Component({
    selector: 'App-point',
    templateUrl: './point.Component.html',
    styleUrls: ['./point.Component.css'],
    providers: [pointService, BottomToastsManager]
})
export class pointComponent {
    Title: string = "point";
    pointEntities: PointEntity[];
    PagingModel = new PagingModel(7, 10, data => {
        this.pointService.Get().subscribe(p => {
            this.pointEntities = p;
        });
    });
    temp = {};
    CreatedpointEntity: PointEntity;
    EditedpointEntity: PointEntity;
    EditPosition: number;
    SearchpointEntity: SearchpointEntity;
    DeleteData: PointEntity;
    EditModalComponent: ModalComponent;
    DeleteModalComponent: ModalComponent;
    problemList: Array<ProblemEntity> = [];
    ChoosedproblemItem: ProblemEntity = new ProblemEntity();
    SearchproblemName: string;

    constructor(private pointService: pointService,
                private toastr: BottomToastsManager, vcr: ViewContainerRef) {
        this.CreatedpointEntity = new PointEntity();
        this.EditedpointEntity = new PointEntity();
        this.SearchpointEntity = new SearchpointEntity();
        this.Search();
        this.EditModalComponent = new ModalComponent();
        this.DeleteModalComponent = new ModalComponent();
        this.toastr.setRootViewContainerRef(vcr);
    }

    Search() {
        this.SearchpointEntity.Skip = this.PagingModel.Take * this.PagingModel.Active;
        this.SearchpointEntity.Take = this.PagingModel.Take;
        this.pointService.Get(this.SearchpointEntity).subscribe(p => {
            this.pointEntities = p;
            this.Count();
        });
    }

    Count() {
        this.pointService.Count(this.SearchpointEntity).subscribe(data => {
            this.PagingModel.TotalPage = Math.ceil(data / this.PagingModel.Take);
        });
    }

    LoadDataToUpdateModal(point: PointEntity, index: number) {
        this.EditedpointEntity = new PointEntity(point);
        this.EditPosition = index;
        document.getElementById(this.EditModalComponent.ID).click();
    }

    LoadDataToDelete(point: PointEntity) {
        this.DeleteData = point;
        document.getElementById(this.DeleteModalComponent.ID).click();
    }

    Edit() {
        this.pointService.Update(this.EditedpointEntity).subscribe(p => {
            this.pointEntities[this.EditPosition] = p;
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Add() {
        this.pointService.Create(this.CreatedpointEntity).subscribe(p => {
            p.IsEdit = true;
            this.pointEntities.unshift(p);
            this.CreatedpointEntity = new PointEntity();
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Delete() {
        this.pointService.Delete(this.DeleteData).subscribe(p => {
            let indexOf = this.pointEntities.indexOf(this.DeleteData);
            this.pointEntities.splice(indexOf, 1);
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    // Save(pointEntity: PointEntity) {
    //     if (pointEntity.Id === undefined || pointEntity.Id === null) {
    //         this.pointService.Create(pointEntity).subscribe(p => {
    //             Object.assign(pointEntity, p);
    //             pointEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     } else {
    //         this.pointService.Update(pointEntity).subscribe(p => {
    //             Object.assign(pointEntity, p);
    //             pointEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     }
    // }
    //
    // Cancel(pointEntity: PointEntity) {
    //     if (pointEntity.Id === undefined || pointEntity.Id === null) {
    //         this.pointEntities.splice(0, 1);
    //     } else {
    //         Object.assign(pointEntity, this.temp);
    //         pointEntity.IsEdit = false;
    //     }
    // }
}