import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/finally';
import {Headers, Http, RequestOptions, RequestOptionsArgs, Response, XHRBackend} from '@angular/http';

declare let $: any;

@Injectable()
export class HttpService extends Http {
    public pendingRequests: number = 0;
    public showLoading: boolean = false;

    constructor(backend: XHRBackend, defaultOptions: RequestOptions) {
        super(backend, defaultOptions);
    }

    get(url: string, options?: RequestOptionsArgs, isShowLoading?: boolean): Observable<Response> {
        if (options == null)
            options = {};
        options.headers = this.GetHeaders();
        return this.intercept(super.get(url, options), isShowLoading);
    }

    post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        if (options == null)
            options = {};
        options.headers = this.GetHeaders();
        return this.intercept(super.post(url, JSON.stringify(body), options));
    }

    put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
        if (options == null)
            options = {};
        options.headers = this.GetHeaders();
        return this.intercept(super.put(url, JSON.stringify(body), options));
    }

    delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
        if (options == null)
            options = {};
        options.headers = this.GetHeaders();
        return this.intercept(super.delete(url, options));
    }

    private intercept(observable: Observable<Response>, isShowLoading?: boolean): Observable<Response> {
        if (isShowLoading == null) {
            console.warn("isShowLoading not setted!");
            isShowLoading = true;
        }
        if (isShowLoading) this.turnOnModal();
        return observable
            .do((res: Response) => {
                console.log("Response: " + res);
            }, (err: any) => {
                if (isShowLoading) this.turnOffModal();
                console.log("Caught error: " + err);
            })
            .finally(() => {
                if (isShowLoading) this.turnOffModal();
            });
    }

    private GetHeaders(): any {
        let headers = new Headers();
        headers.append('content-type', 'application/json');
        return headers;
    }

    private turnOnModal() {
        this.pendingRequests++;
        if (!this.showLoading) {
            this.showLoading = true;
            document.getElementById('SpinnerBar').style.display = 'block';
            console.log("Turned on modal");
        }
        this.showLoading = true;
    }

    private turnOffModal() {
        this.pendingRequests--;
        if (this.pendingRequests <= 0) {
            if (this.showLoading) {
                this.pendingRequests = 0;
                document.getElementById('SpinnerBar').style.display = 'none';
            }
            this.showLoading = false;
        }
        console.log("Turned off modal");
    }
}