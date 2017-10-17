import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { Bill } from './bill.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class BillService {

    private resourceUrl = SERVER_API_URL + 'api/bills';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/bills';

    constructor(private http: Http) { }

    create(bill: Bill): Observable<Bill> {
        const copy = this.convert(bill);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(bill: Bill): Observable<Bill> {
        const copy = this.convert(bill);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Bill> {
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
     * Convert a returned JSON object to Bill.
     */
    private convertItemFromServer(json: any): Bill {
        const entity: Bill = Object.assign(new Bill(), json);
        return entity;
    }

    /**
     * Convert a Bill to a JSON which can be sent to the server.
     */
    private convert(bill: Bill): Bill {
        const copy: Bill = Object.assign({}, bill);
        return copy;
    }
}
