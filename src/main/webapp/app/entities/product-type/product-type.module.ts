import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    ProductTypeService,
    ProductTypePopupService,
    ProductTypeComponent,
    ProductTypeDetailComponent,
    ProductTypeDialogComponent,
    ProductTypePopupComponent,
    ProductTypeDeletePopupComponent,
    ProductTypeDeleteDialogComponent,
    productTypeRoute,
    productTypePopupRoute,
} from './';

const ENTITY_STATES = [
    ...productTypeRoute,
    ...productTypePopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ProductTypeComponent,
        ProductTypeDetailComponent,
        ProductTypeDialogComponent,
        ProductTypeDeleteDialogComponent,
        ProductTypePopupComponent,
        ProductTypeDeletePopupComponent,
    ],
    entryComponents: [
        ProductTypeComponent,
        ProductTypeDialogComponent,
        ProductTypePopupComponent,
        ProductTypeDeleteDialogComponent,
        ProductTypeDeletePopupComponent,
    ],
    providers: [
        ProductTypeService,
        ProductTypePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockProductTypeModule {}
