using System.Text;
using System.Collections.Generic;

namespace AdStir
{
	class LTSV
	{
		private static char[] tab = new char[]{'\t'};
		private static char[] colon = new char[]{ ':' };
		public static Dictionary<string, string> toDictionary(string ltsv)
		{
			var dict = new Dictionary<string, string>();
			var pairs = ltsv.Split(tab);
			
			foreach (var pair in pairs)
			{
				var kv = pair.Split(colon, 2);
				if (kv.Length != 2) continue;
				dict[kv[0]] = kv[1];
			}
			
			return dict;
		}
		
		public static string fromDictionary(Dictionary<string, string> dict)
		{
			var pairs = new string[dict.Count];
			
			int i = 0;
			StringBuilder builder = new StringBuilder();
			
			foreach (var pair in dict)
			{
				builder.Length = 0; // for Unity
				pairs[i] = builder.Append(pair.Key).Append(":").Append(pair.Value).ToString();
				i++;
			}
			
			return string.Join("\t", pairs);
		}
	}
}