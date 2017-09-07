from django.shortcuts import render
import json

# Create your views here.

json_data = open('testapp/Product.json').read()

def get_all_products():
    f = open('testapp/Product.json')
    json_data = f.read()
    f.close()
    all_products = json.loads(json_data)['products']

    return all_products


def index(request):
    if request.method == 'POST':
        _id = request.POST.get('id')
        price = request.POST.get('price')
        name = request.POST.get('name')
        brand = request.POST.get('brand')
        description = request.POST.get('description')
        a_dict = {"id":_id, "price":price, "name":name, "brand":brand, "description":description}

        f = open('testapp/Product.json')
        json_data = f.read()
        f.close()

        new_product = {}
        new_product["products"] = json.loads(json_data)['products']
        new_product["products"].append(a_dict)

        with open('testapp/Product.json', 'w') as f:
            json.dump(new_product, f, indent=4)
            f.close()

        all_products = get_all_products()
        return render(request, 'testapp/bevestiging.html', None)
    else:
        all_products = get_all_products()
        return render(request, 'testapp/index.html', {'all_products': all_products} )

def detail(request, product_id):
    all_products = get_all_products()
    for product in all_products:
        if product['id'] == product_id:
            return render(request, 'testapp/detail.html', {'product': product})

def delete(request, product_id):

    all_products = get_all_products()

    for i in xrange(len(all_products)):
        print(all_products[i])
        if all_products[i]["id"] == product_id:
            del all_products[i]
            break

    new_product = {}
    new_product["products"] = all_products
    with open('testapp/Product.json', 'w') as f:
        json.dump(new_product, f, indent=4)
        f.close()
    all_products = get_all_products()
    return render(request, 'testapp/bevestiging.html', None)
