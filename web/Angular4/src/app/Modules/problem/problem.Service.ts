import 'rxjs/Rx';
import {Observable} from 'rxjs/Rx';
import {Http} from '@angular/http';
import {HttpService} from "../../Shared/HttpService";
import {Injectable} from '@angular/core';
import {ProblemEntity} from "./problem.Entity";
import {SearchproblemEntity} from "./problem.SearchEntity";
import {UserEntity} from "../user/user.Entity";
import {PointEntity} from "../point/point.Entity";
import {ShapeEntity} from "../shape/shape.Entity";
import {HOSTNAME} from "../../app.module";

@Injectable()
export class ProblemService {
    public url: string;

    constructor(private Http: Http) {
        this.url = HOSTNAME + "api/problems";
    }

    GetData(url: string, data: any): Observable<any> {
        let http = <HttpService>this.Http;
        return http.get(url, {params: data}, false)
            .map(res => {
                return res.json();
            });
    }

    Get(Search?: SearchproblemEntity): Observable<any> {
        return this.Http.get(this.url, {params: Search === undefined ? null : Search.ToParams()})
            .map(res => {
                return res.json().map((item: any) => {
                    return new ProblemEntity(item);
                });
            });
    }

    Count(Search?: SearchproblemEntity): Observable<number> {
        Search = Search === undefined ? new SearchproblemEntity() : Search;
        return this.Http.get(this.url + "/Count", {params: Search.ToParams()})
            .map(res => {
                return res.json();
            });
    }

    GetId(Id: string): Observable<any> {
        return this.Http.get(`${this.url}/${Id}`)
            .map(res => {
                return new ProblemEntity(res.json());
            });
    }

    ProcessFile(data: ProblemEntity): Observable<any> {
        return this.Http.get(`${this.url}/${data.problemId}/processfile`)
            .map(res => {
                return res;
            });
    }

    Create(data: ProblemEntity): Observable<any> {
        return this.Http.post(`${this.url}`, data)
            .map(res => {
                return new ProblemEntity(res.json());
            });
    }

    Update(data: ProblemEntity): Observable<any> {
        return this.Http.put(`${this.url}/${data.problemId}`, data)
            .map(res => {
                return new ProblemEntity(res.json());
            });
    }

    Delete(data: ProblemEntity): Observable<any> {
        return this.Http.delete(`${this.url}/${data.problemId}`)
            .catch(e => Observable.throw(e));
    }

    Getuser(problemId: string) {
        return this.Http.get(`${this.url}/${problemId}/user`)
            .map(res => {
                return new UserEntity(res.json());
            });
    }

    Adduser(problemId: string, userId: string) {
        return this.Http.post(`${this.url}/${problemId}/user/${userId}`, {});
    }

    Updateuser(problemId: string, userId: string) {
        return this.Http.put(`${this.url}/${problemId}/user/${userId}`, {});
    }

    Deleteuser(problemId: string, userId: string) {
        return this.Http.delete(`${this.url}/${problemId}/user/${userId}`);
    }

    Getpoint(problemId: string) {
        return this.Http.get(`${this.url}/${problemId}/points`)
            .map(res => {
                return res.json().map(e => new PointEntity(e));
            });
    }

    Addpoint(problemId: string, pointId: string) {
        return this.Http.post(`${this.url}/${problemId}/points/${pointId}`, {});
    }

    Updatepoint(problemId: string, pointId: string) {
        return this.Http.put(`${this.url}/${problemId}/points/${pointId}`, {});
    }

    Deletepoint(problemId: string, pointId: string) {
        return this.Http.delete(`${this.url}/${problemId}/points/${pointId}`);
    }

    Getshape(problemId: string) {
        return this.Http.get(`${this.url}/${problemId}/shapes`)
            .map(res => {
                return res.json().map(e => new ShapeEntity(e));
            });
    }

    Addshape(problemId: string, shapeId: string) {
        return this.Http.post(`${this.url}/${problemId}/shapes/${shapeId}`, {});
    }

    Updateshape(problemId: string, shapeId: string) {
        return this.Http.put(`${this.url}/${problemId}/shapes/${shapeId}`, {});
    }

    Deleteshape(problemId: string, shapeId: string) {
        return this.Http.delete(`${this.url}/${problemId}/shapes/${shapeId}`);
    }
}