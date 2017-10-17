import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SellComponent } from './sell.component';
import { SellDetailComponent } from './sell-detail.component';
import { SellPopupComponent } from './sell-dialog.component';
import { SellDeletePopupComponent } from './sell-delete-dialog.component';

@Injectable()
export class SellResolvePagingParams implements Resolve<any> {

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

export const sellRoute: Routes = [
    {
        path: 'sell',
        component: SellComponent,
        resolve: {
            'pagingParams': SellResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sell.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sell/:id',
        component: SellDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sell.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const sellPopupRoute: Routes = [
    {
        path: 'sell-new',
        component: SellPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sell.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sell/:id/edit',
        component: SellPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sell.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sell/:id/delete',
        component: SellDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sell.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
