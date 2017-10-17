import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    TransferService,
    TransferPopupService,
    TransferComponent,
    TransferDetailComponent,
    TransferDialogComponent,
    TransferPopupComponent,
    TransferDeletePopupComponent,
    TransferDeleteDialogComponent,
    transferRoute,
    transferPopupRoute,
    TransferResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...transferRoute,
    ...transferPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransferComponent,
        TransferDetailComponent,
        TransferDialogComponent,
        TransferDeleteDialogComponent,
        TransferPopupComponent,
        TransferDeletePopupComponent,
    ],
    entryComponents: [
        TransferComponent,
        TransferDialogComponent,
        TransferPopupComponent,
        TransferDeleteDialogComponent,
        TransferDeletePopupComponent,
    ],
    providers: [
        TransferService,
        TransferPopupService,
        TransferResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockTransferModule {}
