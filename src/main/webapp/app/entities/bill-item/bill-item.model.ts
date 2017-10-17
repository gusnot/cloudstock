import { BaseEntity } from './../../shared';

export class BillItem implements BaseEntity {
    constructor(
        public id?: number,
        public sellPrice?: number,
        public sellCost?: number,
        public skuId?: number,
        public billId?: number,
    ) {
    }
}
