import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TransferItemComponent } from './transfer-item.component';
import { TransferItemDetailComponent } from './transfer-item-detail.component';
import { TransferItemPopupComponent } from './transfer-item-dialog.component';
import { TransferItemDeletePopupComponent } from './transfer-item-delete-dialog.component';

export const transferItemRoute: Routes = [
    {
        path: 'transfer-item',
        component: TransferItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transferItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transfer-item/:id',
        component: TransferItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transferItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transferItemPopupRoute: Routes = [
    {
        path: 'transfer-item-new',
        component: TransferItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transferItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transfer-item/:id/edit',
        component: TransferItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transferItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transfer-item/:id/delete',
        component: TransferItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transferItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
