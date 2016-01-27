rem all lists
node get object.json "http://app.formulatx.com/api/static_page?method=getallstaticpagebydisplaymethod&display_method=object"
node get partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagebydisplaymethod&display_method=partner"
node get gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagebydisplaymethod&display_method=gamer"
node get product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagebydisplaymethod&display_method=product"
node get card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagebydisplaymethod&display_method=card"
node get area.json "http://app.formulatx.com/api/static_page?method=getallstaticpagebydisplaymethod&display_method=area"

rem about
node get 201ru.json "http://app.formulatx.com/api/static_page?method=getpage&id=201&lang=ru"
node get 201en.json "http://app.formulatx.com/api/static_page?method=getpage&id=201&lang=en"

rem 205,212,213 tournament
node get 205ru.json "http://app.formulatx.com/api/static_page?method=getpage&id=205&lang=ru"
node get 205en.json "http://app.formulatx.com/api/static_page?method=getpage&id=205&lang=en"
node get 212ru.json "http://app.formulatx.com/api/static_page?method=getpage&id=212&lang=ru"
node get 212en.json "http://app.formulatx.com/api/static_page?method=getpage&id=212&lang=en"
node get 213ru.json "http://app.formulatx.com/api/static_page?method=getpage&id=213&lang=ru"
node get 213en.json "http://app.formulatx.com/api/static_page?method=getpage&id=213&lang=en"

rem additional fields for tournaments 205(225) 212(265) 213(264)
node get 205add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=205"
node get 225add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=225"
node get 212add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=212"
node get 265add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=265"
node get 213add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=213"
node get 264add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=264"


rem 205,212,213 attached objects
node get 205parent.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparent&id=205"
node get 212parent.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparent&id=212"
node get 213parent.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparent&id=213"
node get 265parent.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparent&id=265"
node get 264parent.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparent&id=264"
node get 225parent.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparent&id=225"

rem 205,212,213 attached partner, gamer, product, card
node get 205partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=205&display_method=partner"
node get 212partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=212&display_method=partner"
node get 213partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=213&display_method=partner"
node get 225partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2225&display_method=partner"
node get 264partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2264&display_method=partner"
node get 265partner.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2265&display_method=partner"

node get 205gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=205&display_method=gamer"
node get 212gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=212&display_method=gamer"
node get 213gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=213&display_method=gamer"
node get 225gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2225&display_method=gamer"
node get 264gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2264&display_method=gamer"
node get 265gamer.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2265&display_method=gamer"

node get 205product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=205&display_method=product"
node get 212product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=212&display_method=product"
node get 213product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=213&display_method=product"
node get 225product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2225&display_method=product"
node get 264product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2264&display_method=product"
node get 265product.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2265&display_method=product"

node get 205card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=205&display_method=card"
node get 212card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=212&display_method=card"
node get 213card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=213&display_method=card"
node get 225card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2225&display_method=card"
node get 264card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2264&display_method=card"
node get 265card.json "http://app.formulatx.com/api/static_page?method=getallstaticpagefromparentdisplaymethod&parent=2265&display_method=card"

rem all lists
node get all-object.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=1&num_rec=10&sort_by=date&sort_method=desc&display_method=object"
node get all-partner.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=1&num_rec=10&sort_by=date&sort_method=desc&display_method=partner"
node get all-gamer.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=1&num_rec=10&sort_by=date&sort_method=desc&display_method=gamer"
node get all-product.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=1&num_rec=10&sort_by=date&sort_method=desc&display_method=product"
node get all-card.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=1&num_rec=10&sort_by=date&sort_method=desc&display_method=card"
node get all-area.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=1&num_rec=10&sort_by=date&sort_method=desc&display_method=area"

node get all-object-1.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=2&num_rec=10&sort_by=date&sort_method=desc&display_method=object"
node get all-partner-1.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=2&num_rec=10&sort_by=date&sort_method=desc&display_method=partner"
node get all-gamer-1.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=2&num_rec=10&sort_by=date&sort_method=desc&display_method=gamer"
node get all-product-1.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=2&num_rec=10&sort_by=date&sort_method=desc&display_method=product"
node get all-card-1.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=2&num_rec=10&sort_by=date&sort_method=desc&display_method=card"
node get all-area-1.json "http://app.formulatx.com/api/static_page?method=getAllStaticPageFromDisplayMethod&page=2&num_rec=10&sort_by=date&sort_method=desc&display_method=area"

rem references
node get 57mon.json "http://app.formulatx.com/api/references?method=getreferencebyidapproved&page=1&num_rec=10&sort_by=date&sort_method=desc&id=57"
node get 57mon-1.json "http://app.formulatx.com/api/references?method=getreferencebyidapproved&page=2&num_rec=10&sort_by=date&sort_method=desc&id=57"

rem references
node get 57mon.json "http://app.formulatx.com/api/references?method=getreferencebyidapproved&page=1&num_rec=10&sort_by=date&sort_method=desc&id=57"
node get 57mon-1.json "http://app.formulatx.com/api/references?method=getreferencebyidapproved&page=2&num_rec=10&sort_by=date&sort_method=desc&id=57"

rem list from parent
node static_page news getallstaticpagefromparentnews 205
node static_page news getallstaticpagefromparentnews 212
node static_page news getallstaticpagefromparentnews 213
node static_page news getallstaticpagefromparentnews 225
node static_page news getallstaticpagefromparentnews 264
node static_page news getallstaticpagefromparentnews 265

node static_page gamer getallstaticpagefromparentgamer 205
node static_page gamer getallstaticpagefromparentgamer 212
node static_page gamer getallstaticpagefromparentgamer 213
node static_page gamer getallstaticpagefromparentgamer 225
node static_page gamer getallstaticpagefromparentgamer 264
node static_page gamer getallstaticpagefromparentgamer 265

node static_page card getallstaticpagefromparentcard 0
node static_page product getallstaticpagefromparentproduct 0
node static_page object getallstaticpagefromparentobject 0

rem get product 210(266) and additional fields
node get 210ru.json "http://app.formulatx.com/api/static_page?method=getpage&id=210&lang=ru"
node get 210en.json "http://app.formulatx.com/api/static_page?method=getpage&id=210&lang=en"
node get 210add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=210"
node get 266add.json "http://app.formulatx.com/api/static_page?method=getadditionalfields&id=266"

rem parsers example
node get parsers-19.json "http://app.formulatx.com/api/parsers?method=getparsersbyid&id=19"
node get parsers-20.json "http://app.formulatx.com/api/parsers?method=getparsersbyid&id=20"
node get parsers-22.json "http://app.formulatx.com/api/parsers?method=getparsersbyid&id=22"
node get  parsers-23.json "http://app.formulatx.com/api/parsers?method=getparsersbyid&id=23"
node get  parsers-30.json "http://app.formulatx.com/api/parsers?method=getparsersbyid&id=30"
node get  parsers-31.json "http://app.formulatx.com/api/parsers?method=getparsersbyid&id=31"