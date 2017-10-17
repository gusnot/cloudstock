import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { BillComponent } from './bill.component';
import { BillDetailComponent } from './bill-detail.component';
import { BillPopupComponent } from './bill-dialog.component';
import { BillDeletePopupComponent } from './bill-delete-dialog.component';

@Injectable()
export class BillResolvePagingParams implements Resolve<any> {

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

export const billRoute: Routes = [
    {
        path: 'bill',
        component: BillComponent,
        resolve: {
            'pagingParams': BillResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.bill.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'bill/:id',
        component: BillDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.bill.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const billPopupRoute: Routes = [
    {
        path: 'bill-new',
        component: BillPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.bill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bill/:id/edit',
        component: BillPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.bill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'bill/:id/delete',
        component: BillDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.bill.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
