import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { ProductTypeComponent } from './product-type.component';
import { ProductTypeDetailComponent } from './product-type-detail.component';
import { ProductTypePopupComponent } from './product-type-dialog.component';
import { ProductTypeDeletePopupComponent } from './product-type-delete-dialog.component';

export const productTypeRoute: Routes = [
    {
        path: 'product-type',
        component: ProductTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.productType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'product-type/:id',
        component: ProductTypeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.productType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const productTypePopupRoute: Routes = [
    {
        path: 'product-type-new',
        component: ProductTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.productType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'product-type/:id/edit',
        component: ProductTypePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.productType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'product-type/:id/delete',
        component: ProductTypeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.productType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
