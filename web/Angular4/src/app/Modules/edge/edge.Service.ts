import 'rxjs/Rx';
import {Observable} from 'rxjs/Rx';
import {Http} from '@angular/http';
import {HttpService} from "../../Shared/HttpService";
import {Injectable} from '@angular/core';
import {EdgeEntity} from "./edge.Entity";
import {SearchedgeEntity} from "./edge.SearchEntity";
import {HOSTNAME} from "../../app.module";

@Injectable()
export class EdgeService {
    public url: string;

    constructor(private Http: Http) {
        this.url = HOSTNAME + "api/edges";
    }

    GetData(url: string, data: any): Observable<any> {
        let http = <HttpService>this.Http;
        return http.get(url, {params: data}, false)
            .map(res => {
                return res.json();
            });
    }

    Get(Search?: SearchedgeEntity): Observable<any> {
        return this.Http.get(this.url, {params: Search === undefined ? null : Search.ToParams()})
            .map(res => {
                return res.json().map((item: any) => {
                    return new EdgeEntity(item);
                });
            });
    }

    Count(Search?: SearchedgeEntity): Observable<number> {
        Search = Search === undefined ? new SearchedgeEntity() : Search;
        return this.Http.get(this.url + "/Count", {params: Search.ToParams()})
            .map(res => {
                return res.json();
            });
    }

    GetId(Id: string): Observable<any> {
        return this.Http.get(`${this.url}/${Id}`)
            .map(res => {
                return new EdgeEntity(res.json());
            });
    }

    Create(data: any): Observable<any> {
        return this.Http.post(`${this.url}`, data)
            .map(res => {
                return new EdgeEntity(res.json());
            });
    }

    Update(data: any): Observable<any> {
        return this.Http.put(`${this.url}/${data.Id}`, data)
            .map(res => {
                return new EdgeEntity(res.json());
            });
    }

    Delete(data: any): Observable<any> {
        return this.Http.delete(`${this.url}/${data.Id}`)
            .catch(e => Observable.throw(e));
    }

//		Getshape(edgeId: string) {
//			return this.Http.get(`${this.url}/${edgeId}/shape`)
//				.map(res => {
//					return new shapeEntity(res.json());
//				});
//		}
//		Addshape(edgeId: string, shapeId: string) {
//			return this.Http.post(`${this.url}/${edgeId}/shape/${shapeId}`, {});
//		}
//		Updateshape(edgeId: string, shapeId: string) {
//			return this.Http.put(`${this.url}/${edgeId}/shape/${shapeId}`, {});
//		}
//		Deleteshape(edgeId: string, shapeId: string) {
//			return this.Http.delete(`${this.url}/${edgeId}/shape/${shapeId}`);
//		}
}