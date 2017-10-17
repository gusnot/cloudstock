import { BaseEntity } from './../../shared';

export class Sku implements BaseEntity {
    constructor(
        public id?: number,
        public price?: number,
        public cost?: number,
        public code?: string,
        public barcode?: string,
        public productId?: number,
    ) {
    }
}
