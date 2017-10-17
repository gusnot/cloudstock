import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    SellService,
    SellPopupService,
    SellComponent,
    SellDetailComponent,
    SellDialogComponent,
    SellPopupComponent,
    SellDeletePopupComponent,
    SellDeleteDialogComponent,
    sellRoute,
    sellPopupRoute,
    SellResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...sellRoute,
    ...sellPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        SellComponent,
        SellDetailComponent,
        SellDialogComponent,
        SellDeleteDialogComponent,
        SellPopupComponent,
        SellDeletePopupComponent,
    ],
    entryComponents: [
        SellComponent,
        SellDialogComponent,
        SellPopupComponent,
        SellDeleteDialogComponent,
        SellDeletePopupComponent,
    ],
    providers: [
        SellService,
        SellPopupService,
        SellResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockSellModule {}
