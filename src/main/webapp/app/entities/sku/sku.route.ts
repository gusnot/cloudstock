import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { SkuComponent } from './sku.component';
import { SkuDetailComponent } from './sku-detail.component';
import { SkuPopupComponent } from './sku-dialog.component';
import { SkuDeletePopupComponent } from './sku-delete-dialog.component';

@Injectable()
export class SkuResolvePagingParams implements Resolve<any> {

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

export const skuRoute: Routes = [
    {
        path: 'sku',
        component: SkuComponent,
        resolve: {
            'pagingParams': SkuResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sku.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sku/:id',
        component: SkuDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sku.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const skuPopupRoute: Routes = [
    {
        path: 'sku-new',
        component: SkuPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sku.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sku/:id/edit',
        component: SkuPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sku.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sku/:id/delete',
        component: SkuDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.sku.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
