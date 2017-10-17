import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    TransferItemService,
    TransferItemPopupService,
    TransferItemComponent,
    TransferItemDetailComponent,
    TransferItemDialogComponent,
    TransferItemPopupComponent,
    TransferItemDeletePopupComponent,
    TransferItemDeleteDialogComponent,
    transferItemRoute,
    transferItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...transferItemRoute,
    ...transferItemPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransferItemComponent,
        TransferItemDetailComponent,
        TransferItemDialogComponent,
        TransferItemDeleteDialogComponent,
        TransferItemPopupComponent,
        TransferItemDeletePopupComponent,
    ],
    entryComponents: [
        TransferItemComponent,
        TransferItemDialogComponent,
        TransferItemPopupComponent,
        TransferItemDeleteDialogComponent,
        TransferItemDeletePopupComponent,
    ],
    providers: [
        TransferItemService,
        TransferItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockTransferItemModule {}
