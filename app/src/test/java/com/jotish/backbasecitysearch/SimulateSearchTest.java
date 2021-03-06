package com.jotish.backbasecitysearch;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import com.jotish.backbasecitysearch.models.City;
import com.jotish.backbasecitysearch.models.Coordinates;
import com.jotish.backbasecitysearch.repo.CityRepository;
import com.jotish.backbasecitysearch.trie.TrieMap;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SimulateSearchTest {


  private static final String CITY_TEST_JSON = "[\n"
      + "  {\n"
      + "    \"id\": 707860,\n"
      + "    \"name\": \"Hurzuf\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.283333,\n"
      + "      \"lat\": 44.549999\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 519188,\n"
      + "    \"name\": \"Novinki\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.666668,\n"
      + "      \"lat\": 55.683334\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1283378,\n"
      + "    \"name\": \"Gorkhā\",\n"
      + "    \"country\": \"NP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 84.633331,\n"
      + "      \"lat\": 28\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1270260,\n"
      + "    \"name\": \"State of Haryāna\",\n"
      + "    \"country\": \"IN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 76,\n"
      + "      \"lat\": 29\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 708546,\n"
      + "    \"name\": \"Holubynka\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 33.900002,\n"
      + "      \"lat\": 44.599998\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1283710,\n"
      + "    \"name\": \"Bāgmatī Zone\",\n"
      + "    \"country\": \"NP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 85.416664,\n"
      + "      \"lat\": 28\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 529334,\n"
      + "    \"name\": \"Mar’ina Roshcha\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.611111,\n"
      + "      \"lat\": 55.796391\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1269750,\n"
      + "    \"name\": \"Republic of India\",\n"
      + "    \"country\": \"IN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 77,\n"
      + "      \"lat\": 20\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1283240,\n"
      + "    \"name\": \"Kathmandu\",\n"
      + "    \"country\": \"NP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 85.316666,\n"
      + "      \"lat\": 27.716667\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 703363,\n"
      + "    \"name\": \"Laspi\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 33.733334,\n"
      + "      \"lat\": 44.416668\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3632308,\n"
      + "    \"name\": \"Merida\",\n"
      + "    \"country\": \"VE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -71.144997,\n"
      + "      \"lat\": 8.598333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 473537,\n"
      + "    \"name\": \"Vinogradovo\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 38.545555,\n"
      + "      \"lat\": 55.423332\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 384848,\n"
      + "    \"name\": \"Qarah Gawl al ‘Ulyā\",\n"
      + "    \"country\": \"IQ\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 45.6325,\n"
      + "      \"lat\": 35.353889\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 569143,\n"
      + "    \"name\": \"Cherkizovo\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.728889,\n"
      + "      \"lat\": 55.800835\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 713514,\n"
      + "    \"name\": \"Alupka\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.049999,\n"
      + "      \"lat\": 44.416668\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2878044,\n"
      + "    \"name\": \"Lichtenrade\",\n"
      + "    \"country\": \"DE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 13.40637,\n"
      + "      \"lat\": 52.398441\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 464176,\n"
      + "    \"name\": \"Zavety Il’icha\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.849998,\n"
      + "      \"lat\": 56.049999\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 295582,\n"
      + "    \"name\": \"‘Azriqam\",\n"
      + "    \"country\": \"IL\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.700001,\n"
      + "      \"lat\": 31.75\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1271231,\n"
      + "    \"name\": \"Ghūra\",\n"
      + "    \"country\": \"IN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 79.883331,\n"
      + "      \"lat\": 24.766666\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 690856,\n"
      + "    \"name\": \"Tyuzler\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.083332,\n"
      + "      \"lat\": 44.466667\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 464737,\n"
      + "    \"name\": \"Zaponor’ye\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 38.861942,\n"
      + "      \"lat\": 55.639999\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 707716,\n"
      + "    \"name\": \"Il’ichëvka\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.383331,\n"
      + "      \"lat\": 44.666668\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 697959,\n"
      + "    \"name\": \"Partyzans’ke\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.083332,\n"
      + "      \"lat\": 44.833332\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 803611,\n"
      + "    \"name\": \"Yurevichi\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 39.934444,\n"
      + "      \"lat\": 43.600555\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 614371,\n"
      + "    \"name\": \"Gumist’a\",\n"
      + "    \"country\": \"GE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 40.973888,\n"
      + "      \"lat\": 43.026943\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 874560,\n"
      + "    \"name\": \"Ptitsefabrika\",\n"
      + "    \"country\": \"GE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 40.290558,\n"
      + "      \"lat\": 43.183613\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 874652,\n"
      + "    \"name\": \"Orekhovo\",\n"
      + "    \"country\": \"GE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 40.146111,\n"
      + "      \"lat\": 43.351391\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2347078,\n"
      + "    \"name\": \"Birim\",\n"
      + "    \"country\": \"NG\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 9.997027,\n"
      + "      \"lat\": 10.062094\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2051302,\n"
      + "    \"name\": \"Priiskovyy\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 132.822495,\n"
      + "      \"lat\": 42.819168\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 563692,\n"
      + "    \"name\": \"Dzhaga\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 42.650002,\n"
      + "      \"lat\": 43.25\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 481725,\n"
      + "    \"name\": \"Tret’ya Rota\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 39.681389,\n"
      + "      \"lat\": 43.741943\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2638976,\n"
      + "    \"name\": \"Ruislip\",\n"
      + "    \"country\": \"GB\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -0.42341,\n"
      + "      \"lat\": 51.573441\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2892705,\n"
      + "    \"name\": \"Karow\",\n"
      + "    \"country\": \"DE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 13.48117,\n"
      + "      \"lat\": 52.609039\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2922336,\n"
      + "    \"name\": \"Gatow\",\n"
      + "    \"country\": \"DE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 13.18285,\n"
      + "      \"lat\": 52.483238\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 975511,\n"
      + "    \"name\": \"Mkuze\",\n"
      + "    \"country\": \"ZA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 32.038609,\n"
      + "      \"lat\": -27.616409\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1280737,\n"
      + "    \"name\": \"Lhasa\",\n"
      + "    \"country\": \"CN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 91.099998,\n"
      + "      \"lat\": 29.65\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 745042,\n"
      + "    \"name\": \"İstanbul\",\n"
      + "    \"country\": \"TR\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 28.983311,\n"
      + "      \"lat\": 41.03508\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3496831,\n"
      + "    \"name\": \"Mao\",\n"
      + "    \"country\": \"DO\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -71.078133,\n"
      + "      \"lat\": 19.551861\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2017370,\n"
      + "    \"name\": \"Russian Federation\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 100,\n"
      + "      \"lat\": 60\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2045761,\n"
      + "    \"name\": \"De-Friz\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 131.991394,\n"
      + "      \"lat\": 43.27861\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1257986,\n"
      + "    \"name\": \"Rumbak\",\n"
      + "    \"country\": \"IN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 77.416664,\n"
      + "      \"lat\": 34.049999\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 476350,\n"
      + "    \"name\": \"Vavibet\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 34.916668,\n"
      + "      \"lat\": 67.933334\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1343000,\n"
      + "    \"name\": \"Surtagān Chib\",\n"
      + "    \"country\": \"PK\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 64.656113,\n"
      + "      \"lat\": 26.474443\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 456169,\n"
      + "    \"name\": \"Rīgas Rajons\",\n"
      + "    \"country\": \"LV\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 24.333332,\n"
      + "      \"lat\": 57\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 475279,\n"
      + "    \"name\": \"Verkhneye Shchekotikhino\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 36.133331,\n"
      + "      \"lat\": 53\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 711349,\n"
      + "    \"name\": \"Bucha\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 30.366671,\n"
      + "      \"lat\": 50.583328\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 798544,\n"
      + "    \"name\": \"Republic of Poland\",\n"
      + "    \"country\": \"PL\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 20,\n"
      + "      \"lat\": 52\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3094325,\n"
      + "    \"name\": \"Kuchary\",\n"
      + "    \"country\": \"PL\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 19.383329,\n"
      + "      \"lat\": 52.150002\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 6255149,\n"
      + "    \"name\": \"North America\",\n"
      + "    \"country\": \"UA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -100.546883,\n"
      + "      \"lat\": 46.073231\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3575514,\n"
      + "    \"name\": \"Brumaire\",\n"
      + "    \"country\": \"KN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -62.73333,\n"
      + "      \"lat\": 17.299999\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1861387,\n"
      + "    \"name\": \"Ishikawa-ken\",\n"
      + "    \"country\": \"JP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 136.770493,\n"
      + "      \"lat\": 36.77145\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1857578,\n"
      + "    \"name\": \"Matoba\",\n"
      + "    \"country\": \"JP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 133.949997,\n"
      + "      \"lat\": 34.25\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1299298,\n"
      + "    \"name\": \"Pya\",\n"
      + "    \"country\": \"MM\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 95.599998,\n"
      + "      \"lat\": 21.51667\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3256023,\n"
      + "    \"name\": \"Kalanac\",\n"
      + "    \"country\": \"BA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 18.78389,\n"
      + "      \"lat\": 44.86861\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2921044,\n"
      + "    \"name\": \"Federal Republic of Germany\",\n"
      + "    \"country\": \"DE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 10.5,\n"
      + "      \"lat\": 51.5\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2861876,\n"
      + "    \"name\": \"Land Nordrhein-Westfalen\",\n"
      + "    \"country\": \"DE\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 7,\n"
      + "      \"lat\": 51.5\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 802899,\n"
      + "    \"name\": \"Mutaykutan\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 47.660641,\n"
      + "      \"lat\": 42.818859\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 523523,\n"
      + "    \"name\": \"Nalchik\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 43.618889,\n"
      + "      \"lat\": 43.498058\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 546448,\n"
      + "    \"name\": \"Kolganov\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 40.066669,\n"
      + "      \"lat\": 44.366669\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 500023,\n"
      + "    \"name\": \"Rybatskiy\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 44.166389,\n"
      + "      \"lat\": 44.799171\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2207349,\n"
      + "    \"name\": \"Bellara\",\n"
      + "    \"country\": \"AU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 153.149597,\n"
      + "      \"lat\": -27.063919\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 7870412,\n"
      + "    \"name\": \"Bartlett\",\n"
      + "    \"country\": \"ZA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 28.25263,\n"
      + "      \"lat\": -26.17061\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 961935,\n"
      + "    \"name\": \"Rietfontein\",\n"
      + "    \"country\": \"ZA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 29.200001,\n"
      + "      \"lat\": -25.5\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3371200,\n"
      + "    \"name\": \"Hardap\",\n"
      + "    \"country\": \"NA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 17.25,\n"
      + "      \"lat\": -24.5\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1016666,\n"
      + "    \"name\": \"Botswana\",\n"
      + "    \"country\": \"ZA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 30.533331,\n"
      + "      \"lat\": -24.33333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3858204,\n"
      + "    \"name\": \"El Destierro\",\n"
      + "    \"country\": \"AR\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -62.47662,\n"
      + "      \"lat\": -24.1\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 4070245,\n"
      + "    \"name\": \"Jones Crossroads\",\n"
      + "    \"country\": \"US\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -85.484657,\n"
      + "      \"lat\": 31.21073\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 4344544,\n"
      + "    \"name\": \"Vernon Parish\",\n"
      + "    \"country\": \"US\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -93.183502,\n"
      + "      \"lat\": 31.11685\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 4215307,\n"
      + "    \"name\": \"Pennick\",\n"
      + "    \"country\": \"US\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -81.55899,\n"
      + "      \"lat\": 31.313\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 5285039,\n"
      + "    \"name\": \"Black Bear Spring\",\n"
      + "    \"country\": \"US\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -110.288139,\n"
      + "      \"lat\": 31.386209\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 4673179,\n"
      + "    \"name\": \"Bee House\",\n"
      + "    \"country\": \"US\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -98.081139,\n"
      + "      \"lat\": 31.40266\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 6078447,\n"
      + "    \"name\": \"Morden\",\n"
      + "    \"country\": \"CA\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -98.101357,\n"
      + "      \"lat\": 49.191898\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2201316,\n"
      + "    \"name\": \"Nasirotu\",\n"
      + "    \"country\": \"FJ\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 178.25,\n"
      + "      \"lat\": -18.033331\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1938756,\n"
      + "    \"name\": \"Sisali\",\n"
      + "    \"country\": \"ID\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 124.531387,\n"
      + "      \"lat\": -9.19167\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2009359,\n"
      + "    \"name\": \"Puntan\",\n"
      + "    \"country\": \"ID\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 110.553329,\n"
      + "      \"lat\": -7.51944\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2566086,\n"
      + "    \"name\": \"Tsiémé-Mandiélé\",\n"
      + "    \"country\": \"CG\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 15.2875,\n"
      + "      \"lat\": -4.22694\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 154733,\n"
      + "    \"name\": \"Masama\",\n"
      + "    \"country\": \"TZ\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.183331,\n"
      + "      \"lat\": -3.23333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1630349,\n"
      + "    \"name\": \"Purukcahu\",\n"
      + "    \"country\": \"ID\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 114.583328,\n"
      + "      \"lat\": -0.58333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2224928,\n"
      + "    \"name\": \"Néméyong II\",\n"
      + "    \"country\": \"CM\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 13.5,\n"
      + "      \"lat\": 2.91667\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 6716279,\n"
      + "    \"name\": \"Pondok Genteng\",\n"
      + "    \"country\": \"ID\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 99.0709,\n"
      + "      \"lat\": 3.2245\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2384618,\n"
      + "    \"name\": \"Mbongoté\",\n"
      + "    \"country\": \"CF\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 18.283331,\n"
      + "      \"lat\": 4.25\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 378867,\n"
      + "    \"name\": \"Amiling\",\n"
      + "    \"country\": \"SS\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 32.355831,\n"
      + "      \"lat\": 4.19417\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2230362,\n"
      + "    \"name\": \"Kélkoto\",\n"
      + "    \"country\": \"CM\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 11.16667,\n"
      + "      \"lat\": 4.43333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 343846,\n"
      + "    \"name\": \"Angetu\",\n"
      + "    \"country\": \"ET\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 39.48333,\n"
      + "      \"lat\": 6.33333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 370366,\n"
      + "    \"name\": \"Massa\",\n"
      + "    \"country\": \"SD\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 29.466669,\n"
      + "      \"lat\": 10.98333\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 365618,\n"
      + "    \"name\": \"Tumko\",\n"
      + "    \"country\": \"SD\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 24.6,\n"
      + "      \"lat\": 12.01667\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 524894,\n"
      + "    \"name\": \"Moskva\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.606667,\n"
      + "      \"lat\": 55.761665\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1861060,\n"
      + "    \"name\": \"Japan\",\n"
      + "    \"country\": \"JP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 139.753098,\n"
      + "      \"lat\": 35.68536\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2130037,\n"
      + "    \"name\": \"Hokkaidō\",\n"
      + "    \"country\": \"JP\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 141.346603,\n"
      + "      \"lat\": 43.06451\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 6199126,\n"
      + "    \"name\": \"Sanggrahan\",\n"
      + "    \"country\": \"ID\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 110.246109,\n"
      + "      \"lat\": -7.46056\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 6388445,\n"
      + "    \"name\": \"Karangmangle\",\n"
      + "    \"country\": \"ID\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 109.0075,\n"
      + "      \"lat\": -7.43028\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 494806,\n"
      + "    \"name\": \"Sheremetyevskiy\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 37.491112,\n"
      + "      \"lat\": 55.98\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 467104,\n"
      + "    \"name\": \"Yershovo\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 36.858055,\n"
      + "      \"lat\": 55.771111\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 462352,\n"
      + "    \"name\": \"Znamenka\",\n"
      + "    \"country\": \"RU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 35.981392,\n"
      + "      \"lat\": 52.896671\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 2267057,\n"
      + "    \"name\": \"Lisbon\",\n"
      + "    \"country\": \"PT\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -9.13333,\n"
      + "      \"lat\": 38.716671\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3082707,\n"
      + "    \"name\": \"Walbrzych\",\n"
      + "    \"country\": \"PL\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 16.284321,\n"
      + "      \"lat\": 50.771412\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 3091150,\n"
      + "    \"name\": \"Naklo nad Notecia\",\n"
      + "    \"country\": \"PL\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 17.60181,\n"
      + "      \"lat\": 53.142139\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1784658,\n"
      + "    \"name\": \"Zhengzhou\",\n"
      + "    \"country\": \"CN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 113.648613,\n"
      + "      \"lat\": 34.757778\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 7301040,\n"
      + "    \"name\": \"Tonyrefail\",\n"
      + "    \"country\": \"GB\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": -3.41503,\n"
      + "      \"lat\": 51.580238\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 1348747,\n"
      + "    \"name\": \"Bankra\",\n"
      + "    \"country\": \"IN\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 88.298058,\n"
      + "      \"lat\": 22.627501\n"
      + "    }\n"
      + "  },\n"
      + "  {\n"
      + "    \"id\": 6255148,\n"
      + "    \"name\": \"Europe\",\n"
      + "    \"country\": \"EU\",\n"
      + "    \"coord\": {\n"
      + "      \"lon\": 9.140625,\n"
      + "      \"lat\": 48.69096\n"
      + "    }\n"
      + "  }\n"
      + "]  ";

  @Test
  public void handleSearch() throws Exception {
    List<City> cities = CityRepository.parseJson(CITY_TEST_JSON);
    assertFalse(cities.isEmpty());
    CityRepository.sortList(cities);
    TrieMap<City> searchTrie = CityRepository.buildCityTrieMap(cities);

    String SEARCH_TEST_EMPTY = "Alb";
    List<City> result = CityRepository.onSearch(cities, searchTrie, SEARCH_TEST_EMPTY);
    assertThat(result, hasSize(0));


    String SEARCH_TEST_2 = "Be";
    List<City> result2 = CityRepository.onSearch(cities, searchTrie, SEARCH_TEST_2);
    assertThat(result2, hasSize(2));
    List<City> expected = new ArrayList<>();
    expected.add(new City("AU", "Bellara",1, new Coordinates(1,1)));
    expected.add(new City("US", "Bee House",2, new Coordinates(2,2)));
    assertThat(result2, is(expected));


    String SEARCH_TEST_3 = "A";
    List<City> result3 = CityRepository.onSearch(cities, searchTrie, SEARCH_TEST_3);
    assertThat(result3, hasSize(3));
    List<City> expected3 = new ArrayList<>();
    expected3.add(new City("ET", "Angetu",1, new Coordinates(1,1)));
    expected3.add(new City("SS", "Amiling",2, new Coordinates(2,2)));
    expected3.add(new City("UA", "Alupka",3, new Coordinates(3,3)));
    assertThat(result3, is(expected3));


    String SEARCH_TEST_4 = "Az";
    List<City> result4 = CityRepository.onSearch(cities, searchTrie, SEARCH_TEST_4);
    assertThat(result4, hasSize(0));


    String SEARCH_TEST_INVALID = "~";
    List<City> resultInvalid = CityRepository.onSearch(cities, searchTrie, SEARCH_TEST_INVALID);
    assertThat(resultInvalid, hasSize(0));
  }
}