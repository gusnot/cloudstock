import { BaseEntity } from './../../shared';

export class Attribute implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public attributeItems?: BaseEntity[],
    ) {
    }
}
