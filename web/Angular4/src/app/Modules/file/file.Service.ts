import 'rxjs/Rx';
import {Observable} from 'rxjs/Rx';
import {Http} from '@angular/http';
import {HttpService} from "../../Shared/HttpService";
import {Injectable} from '@angular/core';
import {FileEntity} from "./file.Entity";
import {SearchfileEntity} from "./file.SearchEntity";

@Injectable()
export class FileService {
    public url: string;

    constructor(private Http: Http) {
        this.url = "api/files";
    }

    GetData(url: string, data: FileEntity): Observable<any> {
        let http = <HttpService>this.Http;
        return http.get(url, {params: data}, false)
            .map(res => {
                return res.json();
            });
    }

    Get(Search?: SearchfileEntity): Observable<any> {
        return this.Http.get(this.url, {params: Search === undefined ? null : Search.ToParams()})
            .map(res => {
                return res.json().map((item: any) => {
                    return new FileEntity(item);
                });
            });
    }

    Count(Search?: SearchfileEntity): Observable<number> {
        Search = Search === undefined ? new SearchfileEntity() : Search;
        return this.Http.get(this.url + "/Count", {params: Search.ToParams()})
            .map(res => {
                return res.json();
            });
    }

    GetId(Id: string): Observable<any> {
        return this.Http.get(`${this.url}/${Id}`)
            .map(res => {
                return new FileEntity(res.json());
            });
    }

    Create(data: FileEntity): Observable<any> {
        return this.Http.post(`${this.url}`, data)
            .map(res => {
                return new FileEntity(res.json());
            });
    }

    Update(data: FileEntity): Observable<any> {
        return this.Http.put(`${this.url}/${data.fileId}`, data)
            .map(res => {
                return new FileEntity(res.json());
            });
    }

    Delete(data: FileEntity): Observable<any> {
        return this.Http.delete(`${this.url}/${data.fileId}`)
            .catch(e => Observable.throw(e));
    }

//		Getuser(fileId: string) {
//			return this.Http.get(`${this.url}/${fileId}/user`)
//				.map(res => {
//					return new userEntity(res.json());
//				});
//		}
//		Adduser(fileId: string, userId: string) {
//			return this.Http.post(`${this.url}/${fileId}/user/${userId}`, {});
//		}
//		Updateuser(fileId: string, userId: string) {
//			return this.Http.put(`${this.url}/${fileId}/user/${userId}`, {});
//		}
//		Deleteuser(fileId: string, userId: string) {
//			return this.Http.delete(`${this.url}/${fileId}/user/${userId}`);
//		}
}