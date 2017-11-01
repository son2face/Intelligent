import 'rxjs/Rx';
import {Observable} from 'rxjs/Rx';
import {Http} from '@angular/http';
import {HttpService} from "../../Shared/HttpService";
import {Injectable} from '@angular/core';
import {PointEntity} from "./point.Entity";
import {SearchpointEntity} from "./point.SearchEntity";

@Injectable()
export class pointService {
    public url: string;

    constructor(private Http: Http) {
        this.url = "api/points";
    }

    GetData(url: string, data: any): Observable<any> {
        let http = <HttpService>this.Http;
        return http.get(url, {params: data}, false)
            .map(res => {
                return res.json();
            });
    }

    Get(Search?: SearchpointEntity): Observable<any> {
        return this.Http.get(this.url, {params: Search === undefined ? null : Search.ToParams()})
            .map(res => {
                return res.json().map((item: any) => {
                    return new PointEntity(item);
                });
            });
    }

    Count(Search?: SearchpointEntity): Observable<number> {
        Search = Search === undefined ? new SearchpointEntity() : Search;
        return this.Http.get(this.url + "/Count", {params: Search.ToParams()})
            .map(res => {
                return res.json();
            });
    }

    GetId(Id: string): Observable<any> {
        return this.Http.get(`${this.url}/${Id}`)
            .map(res => {
                return new PointEntity(res.json());
            });
    }

    Create(data: any): Observable<any> {
        return this.Http.post(`${this.url}`, data)
            .map(res => {
                return new PointEntity(res.json());
            });
    }

    Update(data: any): Observable<any> {
        return this.Http.put(`${this.url}/${data.Id}`, data)
            .map(res => {
                return new PointEntity(res.json());
            });
    }

    Delete(data: any): Observable<any> {
        return this.Http.delete(`${this.url}/${data.Id}`)
            .catch(e => Observable.throw(e));
    }

//		Getproblem(pointId: string) {
//			return this.Http.get(`${this.url}/${pointId}/problem`)
//				.map(res => {
//					return new problemEntity(res.json());
//				});
//		}
//		Addproblem(pointId: string, problemId: string) {
//			return this.Http.post(`${this.url}/${pointId}/problem/${problemId}`, {});
//		}
//		Updateproblem(pointId: string, problemId: string) {
//			return this.Http.put(`${this.url}/${pointId}/problem/${problemId}`, {});
//		}
//		Deleteproblem(pointId: string, problemId: string) {
//			return this.Http.delete(`${this.url}/${pointId}/problem/${problemId}`);
//		}
}