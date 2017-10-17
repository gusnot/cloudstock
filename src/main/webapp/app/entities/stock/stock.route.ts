import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { StockComponent } from './stock.component';
import { StockDetailComponent } from './stock-detail.component';
import { StockPopupComponent } from './stock-dialog.component';
import { StockDeletePopupComponent } from './stock-delete-dialog.component';

@Injectable()
export class StockResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const stockRoute: Routes = [
    {
        path: 'stock',
        component: StockComponent,
        resolve: {
            'pagingParams': StockResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'stock/:id',
        component: StockDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const stockPopupRoute: Routes = [
    {
        path: 'stock-new',
        component: StockPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stock/:id/edit',
        component: StockPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'stock/:id/delete',
        component: StockDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.stock.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
