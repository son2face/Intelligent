import {Component, ViewContainerRef} from '@angular/core';
import {BottomToastsManager} from "../../Shared/CustomToaster";
import {PagingModel} from "app/Shared/MaterialComponent/paging/paging.model";
import {ModalComponent} from "../../Shared/MaterialComponent/modal/modal.component";
import {SearchuserEntity} from "./user.SearchEntity";
import {userService} from "./user.Service";
import {UserEntity} from "./user.Entity";
import {ShapeEntity} from "../shape/shape.Entity";
import {ProblemEntity} from "../problem/problem.Entity";
import {FileEntity} from "../file/file.Entity";

@Component({
    selector: 'App-user',
    templateUrl: './user.Component.html',
    styleUrls: ['./user.Component.css'],
    providers: [userService, BottomToastsManager]
})
export class userComponent {
    Title: string = "user";
    userEntities: UserEntity[];
    PagingModel = new PagingModel(7, 10, data => {
        this.userService.Get().subscribe(p => {
            this.userEntities = p;
        });
    });
    temp = {};
    CreateduserEntity: UserEntity;
    EditeduserEntity: UserEntity;
    EditPosition: number;
    SearchuserEntity: SearchuserEntity;
    DeleteData: UserEntity;
    EditModalComponent: ModalComponent;
    DeleteModalComponent: ModalComponent;
    fileList: Array<FileEntity> = [];
    problemList: Array<ProblemEntity> = [];
    shapeList: Array<ShapeEntity> = [];
    ChoosedfileItem: FileEntity = new FileEntity();
    ChoosedproblemItem: ProblemEntity = new ProblemEntity();
    ChoosedshapeItem: ShapeEntity = new ShapeEntity();
    SearchfileName: string;
    SearchproblemName: string;
    SearchshapeName: string;

    constructor(private userService: userService,
                private toastr: BottomToastsManager, vcr: ViewContainerRef) {
        this.CreateduserEntity = new UserEntity();
        this.EditeduserEntity = new UserEntity();
        this.SearchuserEntity = new SearchuserEntity();
        this.Search();
        this.EditModalComponent = new ModalComponent();
        this.DeleteModalComponent = new ModalComponent();
        this.toastr.setRootViewContainerRef(vcr);
    }

    Search() {
        this.SearchuserEntity.Skip = this.PagingModel.Take * this.PagingModel.Active;
        this.SearchuserEntity.Take = this.PagingModel.Take;
        this.userService.Get(this.SearchuserEntity).subscribe(p => {
            this.userEntities = p;
            this.Count();
        });
    }

    Count() {
        this.userService.Count(this.SearchuserEntity).subscribe(data => {
            this.PagingModel.TotalPage = Math.ceil(data / this.PagingModel.Take);
        });
    }

    LoadDataToUpdateModal(user: UserEntity, index: number) {
        this.EditeduserEntity = new UserEntity(user);
        this.EditPosition = index;
        document.getElementById(this.EditModalComponent.ID).click();
    }

    LoadDataToDelete(user: UserEntity) {
        this.DeleteData = user;
        document.getElementById(this.DeleteModalComponent.ID).click();
    }

    Edit() {
        this.userService.Update(this.EditeduserEntity).subscribe(p => {
            this.userEntities[this.EditPosition] = p;
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Add() {
        this.userService.Create(this.CreateduserEntity).subscribe(p => {
            p.IsEdit = true;
            this.userEntities.unshift(p);
            this.CreateduserEntity = new UserEntity();
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Delete() {
        this.userService.Delete(this.DeleteData).subscribe(p => {
            let indexOf = this.userEntities.indexOf(this.DeleteData);
            this.userEntities.splice(indexOf, 1);
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    // Save(userEntity: UserEntity) {
    //     if (userEntity.Id === undefined || userEntity.Id === null) {
    //         this.userService.Create(userEntity).subscribe(p => {
    //             Object.assign(userEntity, p);
    //             userEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     } else {
    //         this.userService.Update(userEntity).subscribe(p => {
    //             Object.assign(userEntity, p);
    //             userEntity.IsEdit = false;
    //             this.toastr.ShowSuccess();
    //         }, e => {
    //             this.toastr.ShowError(e);
    //         });
    //     }
    // }

    // Cancel(userEntity: UserEntity) {
    //     if (userEntity.Id === undefined || userEntity.Id === null) {
    //         this.userEntities.splice(0, 1);
    //     } else {
    //         Object.assign(userEntity, this.temp);
    //         userEntity.IsEdit = false;
    //     }
    // }
}