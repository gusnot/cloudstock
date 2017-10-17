import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CloudstockAttributeModule } from './attribute/attribute.module';
import { CloudstockAttributeItemModule } from './attribute-item/attribute-item.module';
import { CloudstockProductModule } from './product/product.module';
import { CloudstockProductTypeModule } from './product-type/product-type.module';
import { CloudstockSkuModule } from './sku/sku.module';
import { CloudstockBranchModule } from './branch/branch.module';
import { CloudstockStockModule } from './stock/stock.module';
import { CloudstockTransferModule } from './transfer/transfer.module';
import { CloudstockTransferItemModule } from './transfer-item/transfer-item.module';
import { CloudstockTransactionModule } from './transaction/transaction.module';
import { CloudstockTransactionItemModule } from './transaction-item/transaction-item.module';
import { CloudstockBillModule } from './bill/bill.module';
import { CloudstockBillItemModule } from './bill-item/bill-item.module';
import { CloudstockSellModule } from './sell/sell.module';
import { CloudstockAppConfigModule } from './app-config/app-config.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        CloudstockAttributeModule,
        CloudstockAttributeItemModule,
        CloudstockProductModule,
        CloudstockProductTypeModule,
        CloudstockSkuModule,
        CloudstockBranchModule,
        CloudstockStockModule,
        CloudstockTransferModule,
        CloudstockTransferItemModule,
        CloudstockTransactionModule,
        CloudstockTransactionItemModule,
        CloudstockBillModule,
        CloudstockBillItemModule,
        CloudstockSellModule,
        CloudstockAppConfigModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CloudstockEntityModule {}
