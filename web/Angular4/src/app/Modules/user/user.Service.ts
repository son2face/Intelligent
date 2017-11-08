import 'rxjs/Rx';
import {Observable} from 'rxjs/Rx';
import {Http} from '@angular/http';
import {HttpService} from "../../Shared/HttpService";
import {Injectable} from '@angular/core';
import {UserEntity} from "./user.Entity";
import {SearchuserEntity} from "./user.SearchEntity";
import {HOSTNAME} from "../../app.module";
@Injectable()
export class userService {
    public url: string;

    constructor(private Http: Http) {
        this.url = HOSTNAME+"api/users";
    }

    GetData(url: string, data: any): Observable<any> {
        let http = <HttpService>this.Http;
        return http.get(url, {params: data}, false)
            .map(res => {
                return res.json();
            });
    }

    Get(Search?: SearchuserEntity): Observable<any> {
        return this.Http.get(this.url, {params: Search === undefined ? null : Search.ToParams()})
            .map(res => {
                return res.json().map((item: any) => {
                    return new UserEntity(item);
                });
            });
    }

    Count(Search?: SearchuserEntity): Observable<number> {
        Search = Search === undefined ? new SearchuserEntity() : Search;
        return this.Http.get(this.url + "/Count", {params: Search.ToParams()})
            .map(res => {
                return res.json();
            });
    }

    GetId(Id: string): Observable<any> {
        return this.Http.get(`${this.url}/${Id}`)
            .map(res => {
                return new UserEntity(res.json());
            });
    }

    Create(data: any): Observable<any> {
        return this.Http.post(`${this.url}`, data)
            .map(res => {
                return new UserEntity(res.json());
            });
    }

    Update(data: any): Observable<any> {
        return this.Http.put(`${this.url}/${data.Id}`, data)
            .map(res => {
                return new UserEntity(res.json());
            });
    }

    Delete(data: any): Observable<any> {
        return this.Http.delete(`${this.url}/${data.Id}`)
            .catch(e => Observable.throw(e));
    }

//		Getfile(userId: string) {
//			return this.Http.get(`${this.url}/${userId}/files`)
//				.map(res => {
//					return res.json().map(e => new fileEntity(e));
//				});
//		}
//		Addfile(userId: string, fileId: string) {
//			return this.Http.post(`${this.url}/${userId}/files/${fileId}`, {});
//		}
//		Updatefile(userId: string, fileId: string) {
//			return this.Http.put(`${this.url}/${userId}/files/${fileId}`, {});
//		}
//		Deletefile(userId: string, fileId: string) {
//			return this.Http.delete(`${this.url}/${userId}/files/${fileId}`);
//		}
//		Getproblem(userId: string) {
//			return this.Http.get(`${this.url}/${userId}/problems`)
//				.map(res => {
//					return res.json().map(e => new problemEntity(e));
//				});
//		}
//		Addproblem(userId: string, problemId: string) {
//			return this.Http.post(`${this.url}/${userId}/problems/${problemId}`, {});
//		}
//		Updateproblem(userId: string, problemId: string) {
//			return this.Http.put(`${this.url}/${userId}/problems/${problemId}`, {});
//		}
//		Deleteproblem(userId: string, problemId: string) {
//			return this.Http.delete(`${this.url}/${userId}/problems/${problemId}`);
//		}
//		Getshape(userId: string) {
//			return this.Http.get(`${this.url}/${userId}/shapes`)
//				.map(res => {
//					return res.json().map(e => new shapeEntity(e));
//				});
//		}
//		Addshape(userId: string, shapeId: string) {
//			return this.Http.post(`${this.url}/${userId}/shapes/${shapeId}`, {});
//		}
//		Updateshape(userId: string, shapeId: string) {
//			return this.Http.put(`${this.url}/${userId}/shapes/${shapeId}`, {});
//		}
//		Deleteshape(userId: string, shapeId: string) {
//			return this.Http.delete(`${this.url}/${userId}/shapes/${shapeId}`);
//		}
}