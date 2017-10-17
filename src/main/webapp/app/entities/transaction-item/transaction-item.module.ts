import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    TransactionItemService,
    TransactionItemPopupService,
    TransactionItemComponent,
    TransactionItemDetailComponent,
    TransactionItemDialogComponent,
    TransactionItemPopupComponent,
    TransactionItemDeletePopupComponent,
    TransactionItemDeleteDialogComponent,
    transactionItemRoute,
    transactionItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...transactionItemRoute,
    ...transactionItemPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransactionItemComponent,
        TransactionItemDetailComponent,
        TransactionItemDialogComponent,
        TransactionItemDeleteDialogComponent,
        TransactionItemPopupComponent,
        TransactionItemDeletePopupComponent,
    ],
    entryComponents: [
        TransactionItemComponent,
        TransactionItemDialogComponent,
        TransactionItemPopupComponent,
        TransactionItemDeleteDialogComponent,
        TransactionItemDeletePopupComponent,
    ],
    providers: [
        TransactionItemService,
        TransactionItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockTransactionItemModule {}
