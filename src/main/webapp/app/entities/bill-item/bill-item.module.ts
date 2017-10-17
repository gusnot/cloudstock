import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    BillItemService,
    BillItemPopupService,
    BillItemComponent,
    BillItemDetailComponent,
    BillItemDialogComponent,
    BillItemPopupComponent,
    BillItemDeletePopupComponent,
    BillItemDeleteDialogComponent,
    billItemRoute,
    billItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...billItemRoute,
    ...billItemPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        BillItemComponent,
        BillItemDetailComponent,
        BillItemDialogComponent,
        BillItemDeleteDialogComponent,
        BillItemPopupComponent,
        BillItemDeletePopupComponent,
    ],
    entryComponents: [
        BillItemComponent,
        BillItemDialogComponent,
        BillItemPopupComponent,
        BillItemDeleteDialogComponent,
        BillItemDeletePopupComponent,
    ],
    providers: [
        BillItemService,
        BillItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockBillItemModule {}
