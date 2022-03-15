import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemFacturaCompra, ItemFacturaCompra } from '../item-factura-compra.model';

import { ItemFacturaCompraService } from './item-factura-compra.service';

describe('ItemFacturaCompra Service', () => {
  let service: ItemFacturaCompraService;
  let httpMock: HttpTestingController;
  let elemDefault: IItemFacturaCompra;
  let expectedResult: IItemFacturaCompra | IItemFacturaCompra[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemFacturaCompraService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idFacturaCompra: 0,
      idProducto: 0,
      cantidad: 0,
      precio: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ItemFacturaCompra', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ItemFacturaCompra()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemFacturaCompra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idFacturaCompra: 1,
          idProducto: 1,
          cantidad: 1,
          precio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemFacturaCompra', () => {
      const patchObject = Object.assign(
        {
          idFacturaCompra: 1,
          idProducto: 1,
          cantidad: 1,
          precio: 1,
        },
        new ItemFacturaCompra()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemFacturaCompra', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idFacturaCompra: 1,
          idProducto: 1,
          cantidad: 1,
          precio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ItemFacturaCompra', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addItemFacturaCompraToCollectionIfMissing', () => {
      it('should add a ItemFacturaCompra to an empty array', () => {
        const itemFacturaCompra: IItemFacturaCompra = { id: 123 };
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing([], itemFacturaCompra);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemFacturaCompra);
      });

      it('should not add a ItemFacturaCompra to an array that contains it', () => {
        const itemFacturaCompra: IItemFacturaCompra = { id: 123 };
        const itemFacturaCompraCollection: IItemFacturaCompra[] = [
          {
            ...itemFacturaCompra,
          },
          { id: 456 },
        ];
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing(itemFacturaCompraCollection, itemFacturaCompra);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemFacturaCompra to an array that doesn't contain it", () => {
        const itemFacturaCompra: IItemFacturaCompra = { id: 123 };
        const itemFacturaCompraCollection: IItemFacturaCompra[] = [{ id: 456 }];
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing(itemFacturaCompraCollection, itemFacturaCompra);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemFacturaCompra);
      });

      it('should add only unique ItemFacturaCompra to an array', () => {
        const itemFacturaCompraArray: IItemFacturaCompra[] = [{ id: 123 }, { id: 456 }, { id: 33353 }];
        const itemFacturaCompraCollection: IItemFacturaCompra[] = [{ id: 123 }];
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing(itemFacturaCompraCollection, ...itemFacturaCompraArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemFacturaCompra: IItemFacturaCompra = { id: 123 };
        const itemFacturaCompra2: IItemFacturaCompra = { id: 456 };
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing([], itemFacturaCompra, itemFacturaCompra2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemFacturaCompra);
        expect(expectedResult).toContain(itemFacturaCompra2);
      });

      it('should accept null and undefined values', () => {
        const itemFacturaCompra: IItemFacturaCompra = { id: 123 };
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing([], null, itemFacturaCompra, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemFacturaCompra);
      });

      it('should return initial array if no ItemFacturaCompra is added', () => {
        const itemFacturaCompraCollection: IItemFacturaCompra[] = [{ id: 123 }];
        expectedResult = service.addItemFacturaCompraToCollectionIfMissing(itemFacturaCompraCollection, undefined, null);
        expect(expectedResult).toEqual(itemFacturaCompraCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
