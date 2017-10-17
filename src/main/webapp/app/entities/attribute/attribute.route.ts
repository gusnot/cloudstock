import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AttributeComponent } from './attribute.component';
import { AttributeDetailComponent } from './attribute-detail.component';
import { AttributePopupComponent } from './attribute-dialog.component';
import { AttributeDeletePopupComponent } from './attribute-delete-dialog.component';

export const attributeRoute: Routes = [
    {
        path: 'attribute',
        component: AttributeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'attribute/:id',
        component: AttributeDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const attributePopupRoute: Routes = [
    {
        path: 'attribute-new',
        component: AttributePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attribute/:id/edit',
        component: AttributePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'attribute/:id/delete',
        component: AttributeDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'cloudstockApp.attribute.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
