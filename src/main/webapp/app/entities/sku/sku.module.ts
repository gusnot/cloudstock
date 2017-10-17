import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    SkuService,
    SkuPopupService,
    SkuComponent,
    SkuDetailComponent,
    SkuDialogComponent,
    SkuPopupComponent,
    SkuDeletePopupComponent,
    SkuDeleteDialogComponent,
    skuRoute,
    skuPopupRoute,
    SkuResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...skuRoute,
    ...skuPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SkuComponent,
        SkuDetailComponent,
        SkuDialogComponent,
        SkuDeleteDialogComponent,
        SkuPopupComponent,
        SkuDeletePopupComponent,
    ],
    entryComponents: [
        SkuComponent,
        SkuDialogComponent,
        SkuPopupComponent,
        SkuDeleteDialogComponent,
        SkuDeletePopupComponent,
    ],
    providers: [
        SkuService,
        SkuPopupService,
        SkuResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockSkuModule {}
