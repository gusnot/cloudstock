import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';

import { AttributeItem } from './attribute-item.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class AttributeItemService {

    private resourceUrl = SERVER_API_URL + 'api/attribute-items';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/attribute-items';

    constructor(private http: Http) { }

    create(attributeItem: AttributeItem): Observable<AttributeItem> {
        const copy = this.convert(attributeItem);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(attributeItem: AttributeItem): Observable<AttributeItem> {
        const copy = this.convert(attributeItem);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<AttributeItem> {
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
     * Convert a returned JSON object to AttributeItem.
     */
    private convertItemFromServer(json: any): AttributeItem {
        const entity: AttributeItem = Object.assign(new AttributeItem(), json);
        return entity;
    }

    /**
     * Convert a AttributeItem to a JSON which can be sent to the server.
     */
    private convert(attributeItem: AttributeItem): AttributeItem {
        const copy: AttributeItem = Object.assign({}, attributeItem);
        return copy;
    }
}
