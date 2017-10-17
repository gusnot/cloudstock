import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { TransactionItemComponent } from './transaction-item.component';
import { TransactionItemDetailComponent } from './transaction-item-detail.component';
import { TransactionItemPopupComponent } from './transaction-item-dialog.component';
import { TransactionItemDeletePopupComponent } from './transaction-item-delete-dialog.component';

export const transactionItemRoute: Routes = [
    {
        path: 'transaction-item',
        component: TransactionItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transactionItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transaction-item/:id',
        component: TransactionItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transactionItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transactionItemPopupRoute: Routes = [
    {
        path: 'transaction-item-new',
        component: TransactionItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transactionItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transaction-item/:id/edit',
        component: TransactionItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transactionItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transaction-item/:id/delete',
        component: TransactionItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.transactionItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
