import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AttributeItemComponent } from './attribute-item.component';
import { AttributeItemDetailComponent } from './attribute-item-detail.component';
import { AttributeItemPopupComponent } from './attribute-item-dialog.component';
import { AttributeItemDeletePopupComponent } from './attribute-item-delete-dialog.component';

export const attributeItemRoute: Routes = [
    {
        path: 'attribute-item',
        component: AttributeItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attributeItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'attribute-item/:id',
        component: AttributeItemDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attributeItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attributeItemPopupRoute: Routes = [
    {
        path: 'attribute-item-new',
        component: AttributeItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attributeItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attribute-item/:id/edit',
        component: AttributeItemPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attributeItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attribute-item/:id/delete',
        component: AttributeItemDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attributeItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
