import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CloudstockSharedModule } from '../../shared';
import {
    AttributeItemService,
    AttributeItemPopupService,
    AttributeItemComponent,
    AttributeItemDetailComponent,
    AttributeItemDialogComponent,
    AttributeItemPopupComponent,
    AttributeItemDeletePopupComponent,
    AttributeItemDeleteDialogComponent,
    attributeItemRoute,
    attributeItemPopupRoute,
} from './';

const ENTITY_STATES = [
    ...attributeItemRoute,
    ...attributeItemPopupRoute,
];

@NgModule({
    imports: [
        CloudstockSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AttributeItemComponent,
        AttributeItemDetailComponent,
        AttributeItemDialogComponent,
        AttributeItemDeleteDialogComponent,
        AttributeItemPopupComponent,
        AttributeItemDeletePopupComponent,
    ],
    entryComponents: [
        AttributeItemComponent,
        AttributeItemDialogComponent,
        AttributeItemPopupComponent,
        AttributeItemDeleteDialogComponent,
        AttributeItemDeletePopupComponent,
    ],
    providers: [
        AttributeItemService,
        AttributeItemPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockAttributeItemModule {}
