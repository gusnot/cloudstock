import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TransferComponent } from './transfer.component';
import { TransferDetailComponent } from './transfer-detail.component';
import { TransferPopupComponent } from './transfer-dialog.component';
import { TransferDeletePopupComponent } from './transfer-delete-dialog.component';

@Injectable()
export class TransferResolvePagingParams implements Resolve<any> {

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

export const transferRoute: Routes = [
    {
        path: 'transfer',
        component: TransferComponent,
        resolve: {
            'pagingParams': TransferResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transfer/:id',
        component: TransferDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transfer.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transferPopupRoute: Routes = [
    {
        path: 'transfer-new',
        component: TransferPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transfer/:id/edit',
        component: TransferPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transfer/:id/delete',
        component: TransferDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transfer.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
