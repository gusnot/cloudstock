import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Branch } from './branch.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BranchService {

    private resourceUrl = SERVER_API_URL + 'api/branches';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/branches';

    constructor(private http: Http) { }

    create(branch: Branch): Observable<Branch> {
        const copy = this.convert(branch);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(branch: Branch): Observable<Branch> {
        const copy = this.convert(branch);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Branch> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Branch.
     */
    private convertItemFromServer(json: any): Branch {
        const entity: Branch = Object.assign(new Branch(), json);
        return entity;
    }

    /**
     * Convert a Branch to a JSON which can be sent to the server.
     */
    private convert(branch: Branch): Branch {
        const copy: Branch = Object.assign({}, branch);
        return copy;
    }
}
