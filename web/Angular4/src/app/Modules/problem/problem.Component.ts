import {Component, ViewContainerRef} from '@angular/core';
import {BottomToastsManager} from "../../Shared/CustomToaster";
import {PagingModel} from "app/Shared/MaterialComponent/paging/paging.model";
import {ModalComponent} from "../../Shared/MaterialComponent/modal/modal.component";
import {SearchproblemEntity} from "./problem.SearchEntity";
import {ProblemService} from "./problem.Service";
import {ProblemEntity} from "./problem.Entity";
import {DataEntity} from "../../Shared/MaterialComponent/inputfile/Data.Entity";
import {FileService} from "../file/file.Service";
import {FileEntity} from "../file/file.Entity";
import {HOSTNAME} from "../../app.module";

@Component({
    selector: 'App-problem',
    templateUrl: './problem.Component.html',
    styleUrls: ['./problem.Component.css'],
    providers: [ProblemService, BottomToastsManager]
})
export class problemComponent {
    Title: string = "bài toán";
    problemEntities: ProblemEntity[];
    PagingModel = new PagingModel(7, 10, data => {
        this.problemService.Get().subscribe(p => {
            this.problemEntities = p;
        });
    });
    HOSTNAME = HOSTNAME;
    Trick: string = "&nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp";
    CreatedproblemEntity: ProblemEntity;
    EditedproblemEntity: ProblemEntity;
    EditPosition: number;
    SearchproblemEntity: SearchproblemEntity;
    DeleteData: ProblemEntity;
    DataEntity: DataEntity;
    EditModalComponent: ModalComponent;
    // userList: Array<UserEntity> = [];
    // pointList: Array<PointEntity> = [];
    // shapeList: Array<ShapeEntity> = [];
    // ChooseduserItem: UserEntity = new UserEntity();
    // ChoosedpointItem: PointEntity = new PointEntity();
    DeleteModalComponent: ModalComponent;
    // ChoosedshapeItem: ShapeEntity = new ShapeEntity();
    SearchuserName: string;
    SearchpointName: string;
    SearchshapeName: string;

    constructor(private problemService: ProblemService, private fileService: FileService,
                private toastr: BottomToastsManager, vcr: ViewContainerRef) {
        this.DataEntity = new DataEntity();
        this.CreatedproblemEntity = new ProblemEntity();
        this.EditedproblemEntity = new ProblemEntity();
        this.SearchproblemEntity = new SearchproblemEntity();
        this.Search();
        this.EditModalComponent = new ModalComponent();
        this.DeleteModalComponent = new ModalComponent();
        this.toastr.setRootViewContainerRef(vcr);
    }

    Search() {
        this.SearchproblemEntity.Skip = this.PagingModel.Take * this.PagingModel.Active;
        this.SearchproblemEntity.Take = this.PagingModel.Take;
        this.problemService.Get(this.SearchproblemEntity).subscribe(p => {
            this.problemEntities = p;
            this.Count();
        });
    }

    Count() {
        this.problemService.Count(this.SearchproblemEntity).subscribe(data => {
            this.PagingModel.TotalPage = Math.ceil(data / this.PagingModel.Take);
        });
    }

    LoadDataToUpdateModal(problem: ProblemEntity, index: number) {
        this.DataEntity = new DataEntity();
        this.EditedproblemEntity = new ProblemEntity(problem);
        this.EditPosition = index;
        document.getElementById(this.EditModalComponent.ID).click();
    }

    LoadDataToDelete(problem: ProblemEntity) {
        this.DeleteData = problem;
    }

    UploadFile() {
        if (this.DataEntity.length == null || this.DataEntity.length == 0) {
            this.toastr.ShowWarning("Vui lòng chọn file trước khi upload");
            return;
        }
        this.fileService.Create(new FileEntity(this.DataEntity)).subscribe(file => {
            this.EditedproblemEntity.fileId = file.fileId;
            this.EditedproblemEntity.fileEntity = new FileEntity(file);
            this.problemService.Update(this.EditedproblemEntity).subscribe(p => {
                this.problemEntities[this.EditPosition] = p;
                this.toastr.ShowSuccess();
            }, e => {
                this.toastr.ShowError(e);
            });
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    ProcessFile() {
        if (this.EditedproblemEntity.fileId == null) {
            this.toastr.ShowWarning("Vui lòng tải file lên trước!");
            return;
        }
        this.problemService.ProcessFile(this.EditedproblemEntity).subscribe(t => {
            this.toastr.ShowSuccess("Thông tin: " + t + " mảnh ghép");
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Add() {
        this.problemService.Create(this.CreatedproblemEntity).subscribe(p => {
            p.IsEdit = true;
            this.problemEntities.unshift(p);
            this.CreatedproblemEntity = new ProblemEntity();
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Delete() {
        this.problemService.Delete(this.DeleteData).subscribe(p => {
            let indexOf = this.problemEntities.indexOf(this.DeleteData);
            this.problemEntities.splice(indexOf, 1);
            this.toastr.ShowSuccess();
        }, e => {
            this.toastr.ShowError(e);
        });
    }

    Round(number): number {
        return Math.ceil(number);
    }

}