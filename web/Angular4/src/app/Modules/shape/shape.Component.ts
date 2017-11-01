import {Component, ViewContainerRef} from '@angular/core';
import {BottomToastsManager} from "../../Shared/CustomToaster";
import {PagingModel} from "app/Shared/MaterialComponent/paging/paging.model";
import {ModalComponent} from "../../Shared/MaterialComponent/modal/modal.component";
import {SearchshapeEntity} from "./shape.SearchEntity";
import {ShapeService} from "./shape.Service";
import {ShapeEntity} from "./shape.Entity";
import {EdgeEntity} from "../edge/edge.Entity";
import {UserEntity} from "../user/user.Entity";
import {ProblemEntity} from "../problem/problem.Entity";
import {types} from "ngvas";
import Line = types.Line;
import TweenInput = types.TweenInput;

@Component({
    selector: 'App-shape',
    templateUrl: './shape.Component.html',
    styleUrls: ['./shape.Component.css'],
    providers: [ShapeService, BottomToastsManager]
})
export class ShapeComponent {
    Title: string = "shape";
    shapeEntities: any[];
    PagingModel = new PagingModel(7, 10, data => {
        this.shapeService.Get().subscribe(p => {
            this.shapeEntities = p;
        });
    });
    temp = {};
    CreatedshapeEntity: ShapeEntity;
    EditedshapeEntity: ShapeEntity;
    EditPosition: number;
    SearchshapeEntity: SearchshapeEntity;
    DeleteData: ShapeEntity;
    EditModalComponent: ModalComponent;
    DeleteModalComponent: ModalComponent;
    problemList: Array<ProblemEntity> = [];
    userList: Array<UserEntity> = [];
    edgeList: Array<EdgeEntity> = [];
    ChoosedproblemItem: ProblemEntity = new ProblemEntity();
    ChooseduserItem: UserEntity = new UserEntity();
    ChoosededgeItem: EdgeEntity = new EdgeEntity();
    SearchproblemName: string;
    SearchuserName: string;
    SearchedgeName: string;

    constructor(private shapeService: ShapeService,
                private toastr: BottomToastsManager, vcr: ViewContainerRef) {
        this.CreatedshapeEntity = new ShapeEntity();
        this.EditedshapeEntity = new ShapeEntity();
        this.SearchshapeEntity = new SearchshapeEntity();
        this.Search();
        this.EditModalComponent = new ModalComponent();
        this.DeleteModalComponent = new ModalComponent();
        this.toastr.setRootViewContainerRef(vcr);
    }

    Search() {
        this.SearchshapeEntity.Skip = this.PagingModel.Take * this.PagingModel.Active;
        this.SearchshapeEntity.Take = this.PagingModel.Take;
        this.shapeService.Get(this.SearchshapeEntity).subscribe(p => {
            this.shapeEntities = p;
            this.shapeEntities.forEach(h => {
                h.render = [];
                let t: Array<Line> = [];
                h.edgeEntities.forEach(edge => {
                    t.push([[edge.startX, edge.startY], [edge.endX, edge.endY]]);
                });
                h.render.push(t);
            })
            this.Count();
        });
    }

    Count() {
        this.shapeService.Count(this.SearchshapeEntity).subscribe(data => {
            this.PagingModel.TotalPage = Math.ceil(data / this.PagingModel.Take);
        });
    }

    LoadDataToUpdateModal(shape: ShapeEntity, index: number) {
        this.EditedshapeEntity = new ShapeEntity(shape);
        this.EditPosition = index;
        document.getElementById(this.EditModalComponent.ID).click();
    }

    LoadDataToDelete(shape: ShapeEntity) {
        this.DeleteData = shape;
        document.getElementById(this.DeleteModalComponent.ID).click();
    }

    Edit() {
        this.shapeService.Update(this.EditedshapeEntity).subscribe(p => {
            this.shapeEntities[this.EditPosition] = p;
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Add() {
        this.shapeService.Create(this.CreatedshapeEntity).subscribe(p => {
            p.IsEdit = true;
            this.shapeEntities.unshift(p);
            this.CreatedshapeEntity = new ShapeEntity();
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Delete() {
        this.shapeService.Delete(this.DeleteData).subscribe(p => {
            let indexOf = this.shapeEntities.indexOf(this.DeleteData);
            this.shapeEntities.splice(indexOf, 1);
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    // Save(shapeEntity: ShapeEntity) {
    //     if (shapeEntity.Id === undefined || shapeEntity.Id === null) {
    //         this.ShapeService.Create(shapeEntity).subscribe(p => {
    //             Object.assign(shapeEntity, p);
    //             shapeEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     } else {
    //         this.ShapeService.Update(shapeEntity).subscribe(p => {
    //             Object.assign(shapeEntity, p);
    //             shapeEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     }
    // }
    //
    // Cancel(shapeEntity: ShapeEntity) {
    //     if (shapeEntity.Id === undefined || shapeEntity.Id === null) {
    //         this.shapeEntities.splice(0, 1);
    //     } else {
    //         Object.assign(shapeEntity, this.temp);
    //         shapeEntity.IsEdit = false;
    //     }
    // }
}