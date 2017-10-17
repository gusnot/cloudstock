import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    BillService,
    BillPopupService,
    BillComponent,
    BillDetailComponent,
    BillDialogComponent,
    BillPopupComponent,
    BillDeletePopupComponent,
    BillDeleteDialogComponent,
    billRoute,
    billPopupRoute,
    BillResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...billRoute,
    ...billPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BillComponent,
        BillDetailComponent,
        BillDialogComponent,
        BillDeleteDialogComponent,
        BillPopupComponent,
        BillDeletePopupComponent,
    ],
    entryComponents: [
        BillComponent,
        BillDialogComponent,
        BillPopupComponent,
        BillDeleteDialogComponent,
        BillDeletePopupComponent,
    ],
    providers: [
        BillService,
        BillPopupService,
        BillResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockBillModule {}
